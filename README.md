# QuackR

[![CircleCI](https://circleci.com/gh/maximAtanasov/QuackR.svg?style=svg)](https://circleci.com/gh/maximAtanasov/QuackR)

A simple web app built with Spring-Boot and Angular.
Originally built for a university assignment. It can be used as an example
of how Spring-Boot, Apache Shiro, JAX-RS and Angular can be used together to 
create a traditional 3-layer architecture application.

The app allows users to register, log in, create events and post comments.
It can be built by running `mvn install` in the root directory.
The packaged war can afterwards be found in the `quackr-server/target` directory and can 
be started with `java -jar quackr-server-${version}.war`. The app can be accessed on `localhost:8080`
using a web browser.