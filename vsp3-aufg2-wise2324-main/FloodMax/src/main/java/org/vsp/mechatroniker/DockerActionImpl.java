package org.vsp.mechatroniker;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;

import java.io.File;

public class DockerActionImpl implements IDockerActions {
    private final DockerClient dockerClient;

    /**
     * Constructor to initialize the DockerClient.
     *
     * @param dockerClient The DockerClient instance.
     */
    public DockerActionImpl(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * Retrieves the initialized DockerClient.
     *
     * @return The DockerClient instance.
     */
    @Override
    public DockerClient getDockerClient() {
        return dockerClient;
    }

    /**
     * Removes a Docker image by its ID.
     *
     * @param imageID The ID of the Docker image to be removed.
     */
    @Override
    public void removeImage(String imageID) {
        dockerClient.removeImageCmd(imageID).exec();
    }

    /**
     * Builds a Docker image from a Dockerfile at the specified file path.
     *
     * @param filePath The path to the Dockerfile.
     * @return A callback for handling the build result.
     */
    @Override
    public BuildImageResultCallback buildDockerfileImage(String filePath) {
        try {
            return dockerClient.buildImageCmd(new File(filePath)).start().awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a Docker container based on an image ID, name, and exposed port.
     *
     * @param imageID       The ID of the Docker image.
     * @param containerName The name to be assigned to the container.
     * @param exposedPort   The port to be exposed on the container.
     * @return The ID of the created container.
     */
    @Override
    public String createContainer(String imageID, String containerName, int exposedPort) {
        Ports.Binding binding = Ports.Binding.bindPort(exposedPort);
        Ports portBindings = new Ports();
        portBindings.bind(ExposedPort.tcp(exposedPort), binding);

        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageID).withName(containerName).withExposedPorts(ExposedPort.tcp(exposedPort)).withPortBindings(portBindings);

        return createContainerCmd.exec().getId();
    }

    /**
     * Deletes a Docker container by its ID.
     *
     * @param containerId The ID of the Docker container to be deleted.
     */
    @Override
    public void deleteContainer(String containerId) {
        RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(containerId).withForce(true);
        removeContainerCmd.exec();
    }

    /**
     * Starts a Docker container by its ID.
     *
     * @param containerID The ID of the Docker container to be started.
     */
    @Override
    public void startContaienr(String containerID) {
        dockerClient.startContainerCmd(containerID).exec();
    }

    /**
     * Creates a Docker network with the specified name.
     *
     * @param networkName The name of the Docker network.
     * @return The response containing information about the created network.
     */
    @Override
    public CreateNetworkResponse createNetwork(String networkName) {
        return dockerClient.createNetworkCmd().withName(networkName).withDriver("bridge").exec();
    }

    /**
     * Connects two Docker containers to a network by their IDs.
     *
     * @param networkName The name of the Docker network.
     * @param container1  The ID of the first Docker container.
     * @param container2  The ID of the second Docker container.
     */
    @Override
    public void connectTwoContainersWithNetwork(String networkName, String container1, String container2) {
        dockerClient.connectToNetworkCmd().withNetworkId(networkName).withContainerId(container1).exec();
        dockerClient.connectToNetworkCmd().withNetworkId(networkName).withContainerId(container2).exec();
    }

    /**
     * Stops a Docker container by its ID.
     *
     * @param container The ID of the Docker container to be stopped.
     */
    @Override
    public void stopContainer(String container) {
        dockerClient.stopContainerCmd(container).exec();
    }

    /**
     * Removes a Docker network by its name.
     *
     * @param networkName The name of the Docker network to be removed.
     */
    @Override
    public void removeNetwork(String networkName) {
        dockerClient.removeNetworkCmd(networkName).exec();
    }

    /**
     * Pulls a Docker image by its name.
     *
     * @param imageName The name of the Docker image to be pulled.
     * @throws InterruptedException If the image pull operation is interrupted.
     */
    @Override
    public void pullImage(String imageName) throws InterruptedException {
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(imageName);
        pullImageCmd.exec(new PullImageResultCallback()).awaitCompletion();
    }


}
