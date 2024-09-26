package edu.ucne.davidrosario_p1_ap2.presentation.venta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity

@Composable
fun VentaListScreen(
    viewModel: VentaViewModel = hiltViewModel(),
    onVentaClick: (Int) -> Unit,
    onVentaAdd: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VentaListBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onVentaClick = onVentaClick,
        onVentaAdd = onVentaAdd,
        onVentaDelete = { ventaId ->
            viewModel.onEvent(
                VentaUiEvent.selectedVenta(ventaId)
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
    onEvent: (VentaUiEvent) -> Unit,
    onVentaClick: (Int) -> Unit,
    onVentaAdd: () -> Unit,
    onVentaDelete: (Int) -> Unit
){
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
                if(uiState.ventas.isEmpty()){
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
                    items(uiState.ventas){
                        VentaRow(
                            it = it,
                            onVentaClick = onVentaClick,
                            onVentaDelete = onVentaDelete
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
    onVentaDelete: (Int) -> Unit
){
    Card(
        onClick = {
            onVentaClick(it.ventaId ?: 0)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 10.dp)
            .heightIn(160.dp)
    ){
        Row {
            Column {
                Text(it.cliente)
                Text(it.galones.toString())
                Text(it.descuentoGalon.toString())
                Text(it.precio.toString())
                Text(it.totalDescontado.toString())
                Text(it.total.toString())
            }

            IconButton(
                onClick = {
                    onVentaDelete(it.ventaId ?: 0)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Venta"
                )
            }
        }
    }
}