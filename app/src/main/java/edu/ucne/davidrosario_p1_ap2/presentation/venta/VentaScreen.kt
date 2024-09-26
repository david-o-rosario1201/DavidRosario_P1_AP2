@file:OptIn(ExperimentalMaterial3Api::class)

package edu.ucne.davidrosario_p1_ap2.presentation.venta

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun VentaScreen(
    viewModel: VentaViewModel = hiltViewModel(),
    ventaId: Int,
    goVentaList: () -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VentaBodyScreen(
        ventaId = ventaId,
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goVentaList = goVentaList
    )
}

@Composable
fun VentaBodyScreen(
    ventaId: Int,
    uiState: VentaUiState,
    onEvent: (VentaUiEvent) -> Unit,
    goVentaList: () -> Unit
){
    var inputGalones by remember { mutableStateOf("") }
    var inputDescuentoGalon by remember { mutableStateOf("") }
    var inputPrecio by remember { mutableStateOf("") }


    LaunchedEffect(
        key1 = true,
        key2 = uiState.success
    ) {
//        if(!inputGalones.isNullOrEmpty() && !inputDescuentoGalon.isNullOrEmpty() && !inputPrecio.isNullOrEmpty()){
//            val totalDescontado = inputGalones.toDouble() * inputDescuentoGalon.toDouble()
//            onEvent(VentaUiEvent.totalDescontadoChanged(totalDescontado))
//
//            val total = (inputGalones.toDouble() * inputPrecio.toDouble()) - totalDescontado
//            onEvent(VentaUiEvent.totalChanged(total))
//        }

        if(uiState.success)
            goVentaList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if(ventaId == 0) "Crear Venta" else "Modificar Venta",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = goVentaList
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Ir a la Lista de Ventas"
                        )
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    label = {
                        Text("Datos del Cliente")
                    },
                    value = uiState.cliente ?: "",
                    onValueChange = {
                        onEvent(VentaUiEvent.clienteChanged(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp)
                )
                uiState.errorCliente?.let {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    label = {
                        Text("Galones")
                    },
                    value = uiState.galones,
                    onValueChange = {
                        onEvent(VentaUiEvent.galonesChanged(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
                uiState.errorGalones?.let {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    label = {
                        Text("Descuento por galón")
                    },
                    value = uiState.descuentoGalon,
                    onValueChange = {
                        onEvent(VentaUiEvent.descuentoGalonChanged(it))
//                        if(it.isEmpty()){
//                            inputDescuentoGalon = ""
//                            onEvent(VentaUiEvent.galonesChanged(null))
//                        }
//                        else if(!it.endsWith(".")){
//                            inputDescuentoGalon = it
//                            val descuentoGalon = it.toDoubleOrNull()
//                            if(descuentoGalon != null){
//                                onEvent(VentaUiEvent.galonesChanged(descuentoGalon))
////                                if(!inputGalones.isNullOrEmpty() && !inputDescuentoGalon.isNullOrEmpty() && !inputPrecio.isNullOrEmpty()){
////                                    val totalDescontado = inputGalones.toDouble() * inputDescuentoGalon.toDouble()
////                                    onEvent(VentaUiEvent.totalDescontadoChanged(totalDescontado))
////
////                                    val total = (inputGalones.toDouble() * inputPrecio.toDouble()) - totalDescontado
////                                    onEvent(VentaUiEvent.totalChanged(total))
////                                }
//                            }
//                        }
//                        else
//                            inputDescuentoGalon = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
                uiState.errorDescuentoGalon?.let {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                OutlinedTextField(
                    label = {
                        Text("Precio")
                    },
                    value = uiState.precio,
                    onValueChange = {
                        onEvent(VentaUiEvent.precioChanged(it))
//                        if(it.isEmpty()){
//                            inputPrecio = ""
//                            onEvent(VentaUiEvent.galonesChanged(null))
//                        }
//                        else if(!it.endsWith(".")){
//                            inputPrecio = it
//                            val precio = it.toDoubleOrNull()
//                            if(precio != null){
//                                onEvent(VentaUiEvent.galonesChanged(precio))
////                                if(!inputGalones.isNullOrEmpty() && !inputDescuentoGalon.isNullOrEmpty() && !inputPrecio.isNullOrEmpty()){
////                                    val totalDescontado = inputGalones.toDouble() * inputDescuentoGalon.toDouble()
////                                    onEvent(VentaUiEvent.totalDescontadoChanged(totalDescontado))
////
////                                    val total = (inputGalones.toDouble() * inputPrecio.toDouble()) - totalDescontado
////                                    onEvent(VentaUiEvent.totalChanged(total))
////                                }
//                            }
//                        }
//                        else
//                            inputPrecio = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )
                uiState.errorPrecio?.let {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }

            OutlinedTextField(
                label = {
                    Text("Total Descontado")
                },
                value = uiState.totalDescontado?.toString() ?: "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                readOnly = true
            )
            OutlinedTextField(
                label = {
                    Text("Total")
                },
                value = uiState.total?.toString() ?: "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
                readOnly = true
            )

            OutlinedButton(
                onClick = {
                    onEvent(VentaUiEvent.Save)
                }
            ) {
                Text(
                    text = if(ventaId == 0) "Crear Venta" else "Modificar Venta"
                )
            }
        }
    }
}