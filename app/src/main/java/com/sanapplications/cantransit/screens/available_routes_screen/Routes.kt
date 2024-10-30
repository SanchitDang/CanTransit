import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sanapplications.cantransit.screens.available_routes_screen.RoutesViewModel

@Composable
fun RouteScreen(
    origin: String,
    destination: String,
    apiKey: String
) {
    val viewModel: RoutesViewModel = viewModel()
    val routeState by viewModel.routeState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchRoute(origin, destination, apiKey)
    }

    when {
        routeState?.isSuccess == true -> {
            val route = routeState?.getOrNull()
            // Display the route information here
        }
        routeState?.isFailure == true -> {
            val exception = routeState?.exceptionOrNull()
            // Display the error message here using exception.message
        }
        else -> {
            // Display a loading or idle state
        }
    }
}
