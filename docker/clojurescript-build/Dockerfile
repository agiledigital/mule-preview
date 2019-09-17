FROM circleci/clojure:lein-2.9.1

# For some reason I couldn't find a build image with Clojure/JVM and an up
# to date version of Node in one image
# TODO: Clean this up and add CI/CD

SHELL ["/bin/bash", "-eo", "pipefail", "-c"]
USER root
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash -
RUN apt-get -y --no-install-recommends install nodejs=10.16.3-1nodesource1
USER circleci
CMD ["/bin/bash"]
