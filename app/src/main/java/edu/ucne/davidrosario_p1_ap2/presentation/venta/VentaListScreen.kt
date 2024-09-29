@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package edu.ucne.davidrosario_p1_ap2.presentation.venta

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity
import edu.ucne.davidrosario_p1_ap2.presentation.navigation.Screen
import edu.ucne.davidrosario_p1_ap2.ui.theme.DavidRosario_P1_AP2Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VentaListScreen(
    viewModel: VentaViewModel = hiltViewModel(),
    onVentaClick: (Int) -> Unit,
    onVentaAdd: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VentaListBodyScreen(
        uiState = uiState,
        onVentaClick = onVentaClick,
        onVentaAdd = onVentaAdd,
        onVentaDelete = { ventaId ->
            viewModel.onEvent(
                VentaUiEvent.ventaIdChanged(ventaId)
            )
            viewModel.onEvent(
                VentaUiEvent.Delete
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaListBodyScreen(
    uiState: VentaUiState,
    onVentaClick: (Int) -> Unit,
    onVentaAdd: () -> Unit,
    onVentaDelete: (Int) -> Unit
){
    val ventas = remember { mutableStateListOf(*uiState.ventas.toTypedArray()) }

    LaunchedEffect(uiState.ventas) {
        ventas.clear()
        ventas.addAll(uiState.ventas)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Ventas",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onVentaAdd
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Agregar Nueva Venta"
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                if(ventas.isEmpty()){
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Lista vacía",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                else{
                    items(
                        items = ventas,
                        key = { it.ventaId ?: 0}
                    ){
                        VentaRow(
                            it = it,
                            onVentaClick = onVentaClick,
                            onVentaDelete = onVentaDelete,
                            modifier = Modifier.animateItemPlacement(tween(200))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VentaRow(
    it: VentaEntity,
    onVentaClick: (Int) -> Unit,
    onVentaDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val scope = rememberCoroutineScope()
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            if(state == SwipeToDismissBoxValue.EndToStart){
                scope.launch {
                    delay(500)
                    onVentaDelete(it.ventaId ?: 0)
                }
                true
            } else{
                false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when (swipeToDismissState.currentValue) {
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    SwipeToDismissBoxValue.Settled -> Color.White
                }, label = ""
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
            )
        },
        modifier = modifier
    ) {
        Card(
            onClick = {
                onVentaClick(it.ventaId ?: 0)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(horizontal = 10.dp)
                .height(160.dp)
        ){
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ){
                Column(
                    modifier = Modifier
                ){
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ){
                        Text(
                            text = "Cliente: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.cliente)
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ){
                        Text(
                            text = "Galones: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.galones.toString())
                    }
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ){
                        Text(
                            text = "Descuento por Galón: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.descuentoGalon.toString())
                    }
                }

                Column {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ){
                        Text(
                            text = "Precio: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.precio.toString())
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ){
                        Text(
                            text = "Total Descontado: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.totalDescontado.toString())
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                    ){
                        Text(
                            text = "Total: ",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(it.total.toString())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VentaListScreenPreview() {
    DavidRosario_P1_AP2Theme {
        val navHostController = rememberNavController()
        VentaListScreen(
            onVentaClick = { ventaId ->
                navHostController.navigate(Screen.VentaScreen(ventaId))
            },
            onVentaAdd = {
                navHostController.navigate(Screen.VentaScreen(0))
            }
        )
    }
}