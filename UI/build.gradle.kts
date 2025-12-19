plugins {
    id("java")
}

group = "com.sahdkhan"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Adding the backend module here
    implementation(project(":Backend"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}