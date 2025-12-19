/**
 * This class is for ALL the modules in the project i.e UI, Backend, app.
 * Do not add dependencies here unless all the modules need them.
 */

plugins {
    id("java")
}

group = "com.sahdkhan"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}