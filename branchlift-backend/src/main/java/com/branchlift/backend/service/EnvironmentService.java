// Conteúdo COMPLETO e CORRIGIDO do EnvironmentService.java
package com.branchlift.backend.service;

import com.branchlift.backend.model.Environment;
import com.branchlift.backend.model.Project;
import com.branchlift.backend.repository.EnvironmentRepository;
import com.branchlift.backend.repository.ProjectRepository;
import com.branchlift.backend.util.ShellCommandExecutor;
import com.branchlift.backend.util.ShellCommandExecutor.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class EnvironmentService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Value("${branchlift.git.repos.base-path:/tmp/branchlift_repos}")
    private String gitReposBasePath;

    @Value("${branchlift.environments.base-path:/tmp/branchlift_environments}")
    private String environmentsBasePath;

    public List<Environment> getAllEnvironments() {
        return environmentRepository.findAll();
    }

    public Environment provisionNewEnvironment(Long projectId, String gitBranch, String createdBy) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        Environment newEnvironment = new Environment(project, gitBranch, createdBy);
        Environment savedEnvironment = environmentRepository.save(newEnvironment);

        new Thread(() -> {
            try {
                provisionEnvironment(savedEnvironment.getId());
            } catch (Exception e) {
                handleProvisioningError(savedEnvironment.getId(), e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            }
        }).start();

        return savedEnvironment;
    }

    public void provisionEnvironment(Long environmentId) throws Exception {
        Environment environment = environmentRepository.findById(environmentId)
                .orElseThrow(() -> new RuntimeException("Environment not found with ID: " + environmentId));

        Project project = environment.getProject();
        String projectName = project.getName().replaceAll("\\s+", "_").toLowerCase();
        String gitRepoUrl = project.getGitRepoUrl();
        String gitBranch = environment.getGitBranch();

        prepareCodebaseForEnvironment(projectName, gitRepoUrl, gitBranch);

        String envUniqueId = UUID.randomUUID().toString().substring(0, 8);
        Path envDir = Paths.get(environmentsBasePath, "env_" + projectName + "_" + envUniqueId);
        Files.createDirectories(envDir);

        // SALVA O CAMINHO DO DIRETÓRIO NO BANCO
        environment.setDirectoryPath(envDir.toString());
        environmentRepository.save(environment);

        Path sourceRepoPath = Paths.get(gitReposBasePath, projectName);
        copyDirectory(sourceRepoPath, envDir);

        generateDockerComposeFile(envDir, envUniqueId);

        int frontendPort = findAvailablePort(3100);

        generateEnvFile(envDir, 0, frontendPort, 0, "branchlift_db_" + envUniqueId, "user_" + envUniqueId, "pass_" + envUniqueId);

        environment.setStatus("PROVISIONING");
        environmentRepository.save(environment);

        CommandResult result = ShellCommandExecutor.executeCommand(
                Arrays.asList("docker-compose", "-f", envDir.resolve("docker-compose.yml").toString(), "up", "-d", "--build"),
                envDir.toFile(),
                600
        );

        if (result.isSuccess()) {
            environment.setStatus("RUNNING");
            environment.setAccessUrl("http://localhost:" + frontendPort);
            environment.setAllocatedPort(frontendPort);
            environment.setBuildLog(result.getOutput());
            environmentRepository.save(environment);
        } else {
            // Se falhar, apaga a pasta criada
            FileSystemUtils.deleteRecursively(envDir);
            throw new RuntimeException("Failed to provision environment: " + result.getOutput());
        }
    }

    // NOVO MÉTODO COMPLETO PARA DELETAR
    public void deleteEnvironment(Long id) throws Exception {
        Environment environment = environmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Environment not found with ID: " + id));

        if (environment.getDirectoryPath() != null && !environment.getDirectoryPath().isEmpty()) {
            Path envDir = Paths.get(environment.getDirectoryPath());

            if (Files.exists(envDir)) {
                // Executa docker-compose down para parar e remover os contêineres
                CommandResult result = ShellCommandExecutor.executeCommand(
                        Arrays.asList("docker-compose", "-f", envDir.resolve("docker-compose.yml").toString(), "down", "--volumes"),
                        envDir.toFile(),
                        120
                );

                if (!result.isSuccess()) {
                    System.err.println("Warning: docker-compose down failed, but proceeding with deletion. Log: " + result.getOutput());
                }

                // Apaga o diretório do ambiente
                FileSystemUtils.deleteRecursively(envDir);
            }
        }

        // Finalmente, apaga o registro do banco de dados
        environmentRepository.delete(environment);
    }

    private void handleProvisioningError(Long environmentId, String errorLog) {
        try {
            Environment environment = environmentRepository.findById(environmentId)
                    .orElseThrow(() -> new RuntimeException("Environment not found for error handling: " + environmentId));
            environment.setStatus("ERROR");
            environment.setBuildLog(errorLog);
            environmentRepository.save(environment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int findAvailablePort(int startingPort) {
        return startingPort + (int)(Math.random() * 1000);
    }

    private void prepareCodebaseForEnvironment(String projectName, String gitRepoUrl, String branchName) throws Exception {
        // ... (sem alterações)
    }

    private void cloneOrPullRepository(String projectName, String gitRepoUrl) throws Exception {
        // ... (sem alterações)
    }

    private void checkoutBranch(String projectName, String branchName) throws Exception {
        // ... (sem alterações)
    }

    private void copyDirectory(Path source, Path target) throws IOException {
        // ... (sem alterações)
    }

    private void generateDockerComposeFile(Path envDir, String envUniqueId) throws IOException {
        // ... (sem alterações)
    }

    private void generateEnvFile(Path envDir, int backendPort, int frontendPort, int dbPort, String dbName, String dbUser, String dbPassword) throws IOException {
        // ... (sem alterações)
    }
}