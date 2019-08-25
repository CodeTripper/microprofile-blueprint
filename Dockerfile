FROM payara/micro

COPY target/pykara.war $DEPLOY_DIR
CMD ["--deploymentDir", "/opt/payara/deployments", "--contextroot", "/"]