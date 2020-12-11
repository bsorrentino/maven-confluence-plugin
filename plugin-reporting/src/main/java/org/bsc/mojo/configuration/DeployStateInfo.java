package org.bsc.mojo.configuration;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class DeployStateInfo {

    private Path outdir = null;
    private boolean active;

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @param outdir the basedir to set
     */
    public void setOutdir(java.io.File outdir) {
        if( outdir != null ) {
            this.outdir = outdir.toPath();
        }
    }

    public Path getOutdir() {
        Objects.requireNonNull( outdir, "outdir is null!");
        return outdir;
    }

    /**
     * @return the _outdir
     */
    public Optional<Path> optOutdir() {
        return ofNullable(outdir);
    }

    public DeployStateInfo() {
        this(true);
    }

    public DeployStateInfo(boolean active) {
        this.active = active;
    }

    /**
     *
     */
    @Override
    public String toString() {
        final String dir = optOutdir().map( p -> p.toString()).orElse("<null>");

        return new StringBuilder()
                .append("DeployStateInfo").append("\n\t")
                .append("active=").append(active).append("\n\t")
                .append("outdir=").append(dir).append("\n")
                .toString();
    }


}
