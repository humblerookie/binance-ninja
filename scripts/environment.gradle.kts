import java.io.FileInputStream
import java.util.Properties

private val secretProperties = Properties()

with(file("../secrets.properties")) {
  if (exists()) {
    secretProperties.load(FileInputStream(this))
  }
}
extra["bundleId"] = "dev.anvith.binanceninja"
extra["java"] = "17"
extra["getEnv"] =
  fun(key: String): String {
    return secretProperties[key]?.toString() ?: System.getenv(key).orEmpty()
  }
