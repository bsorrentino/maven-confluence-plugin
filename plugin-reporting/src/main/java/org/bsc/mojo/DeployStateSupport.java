package org.bsc.mojo;

import org.apache.maven.project.MavenProject;
import org.bsc.confluence.DeployStateManager;
import org.bsc.mojo.configuration.DeployStateInfo;

import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

/**
 *
 */
public interface DeployStateSupport {

    MavenProject getProject();

    String getEndPoint();

    DeployStateInfo getDeployState();

    Optional<DeployStateManager> getDeployStateManager();

    default Optional<DeployStateManager> initDeployStateManager() {

        if( !getDeployState().isActive() ) return empty();

        if (!getDeployState().optOutdir().isPresent()) {
            getDeployState().setOutdir(Paths.get(getProject().getBuild().getDirectory()).toFile());
        }

        return of(DeployStateManager.load(getEndPoint(), getDeployState().getOutdir()));

    }

}
