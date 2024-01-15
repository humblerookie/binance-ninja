import java.io.FileInputStream
import java.util.Properties

private val secretProperties = Properties()

with(file("local.properties")) {
  if (exists()) {
    secretProperties.load(FileInputStream(this))
  }
}

extra["getEnv"] =
  fun(key: String): String {
    return secretProperties[key]?.toString() ?: System.getenv(key).orEmpty()
  }
