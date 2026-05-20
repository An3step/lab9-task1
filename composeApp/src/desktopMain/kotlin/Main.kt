import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.task_9_1.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Weather App") {
        App()
    }
}
