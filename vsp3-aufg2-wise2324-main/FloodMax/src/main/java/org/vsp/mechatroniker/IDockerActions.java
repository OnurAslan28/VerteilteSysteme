package org.vsp.mechatroniker;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateNetworkResponse;

public interface IDockerActions {

    DockerClient getDockerClient();

    void removeImage(String imageID);

    BuildImageResultCallback buildDockerfileImage(String filePath);

    // Container erstellen
    String createContainer(String imageID, String containerName, int exposedPort);


    // Container löschen
    void deleteContainer(String containerId);

    void pullImage(String imageName) throws InterruptedException;
    void startContaienr(String containerID);

    CreateNetworkResponse createNetwork(String networkName);
    void connectTwoContainersWithNetwork(String networkName, String container1, String container2);

    void stopContainer(String container);

    void removeNetwork(String networkName);
}
