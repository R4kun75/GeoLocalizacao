package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.coroutines.launch
import org.example.project.data.LocationRepository

@Composable
fun App() {

    MaterialTheme {

        val permissionsFactory = rememberPermissionsControllerFactory()
        val permissionsController = remember {
            permissionsFactory.createPermissionsController()
        }

        BindEffect(permissionsController)

        val geoFactory = rememberLocationTrackerFactory(
            accuracy = LocationTrackerAccuracy.Best
        )

        val locationTracker = remember {
            geoFactory.createLocationTracker(
                permissionsController
            )
        }

        BindLocationTrackerEffect(locationTracker)

        val locationRepository = remember {
            LocationRepository(locationTracker)
        }

        val coroutineScope = rememberCoroutineScope()

        var hasPermission by remember {
            mutableStateOf(false)

        }

        var locationText by remember {
            mutableStateOf("Obtendo localização... Aguarde alguns segundos.")
        }

        LaunchedEffect(hasPermission) {

            if (hasPermission) {

                try {

                    locationText = "Iniciando rastreamento..."

                    locationRepository.startTracking()

                    locationText = "Rastreamento iniciado. Aguardando GPS..."

                    locationRepository
                        .getLocationUpdates()
                        .collect { location ->

                            locationText =
                                "Latitude: ${location.latitude}\n" +
                                        "Longitude: ${location.longitude}"
                        }

                } catch (e: Throwable) {

                    locationText =
                        "Erro: ${e.message ?: "desconhecido"}"
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📍 Dashboard de Coordenadas",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Text(
                        text = "Latitude e Longitude atuais:",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = locationText,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            if (!hasPermission) {

                Text(
                    "Precisamos da sua localização para o Dashboard."
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {

                        coroutineScope.launch {

                            try {

                                permissionsController
                                    .providePermission(
                                        Permission.LOCATION
                                    )

                                hasPermission = true

                            } catch (e: Throwable) {

                                locationText =
                                    "Permissão negada."
                            }
                        }
                    }
                ) {
                    Text("Conceder Permissão")
                }

            } else {

                Button(
                    onClick = {

                        locationRepository.stopTracking()

                        locationText =
                            "Rastreamento interrompido."
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Parar Rastreio")
                }
            }
        }
    }
}