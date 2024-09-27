package edu.ucne.davidrosario_p1_ap2.presentation.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.davidrosario_p1_ap2.data.local.entities.VentaEntity
import edu.ucne.davidrosario_p1_ap2.data.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentaViewModel @Inject constructor(
    private val ventaRepository: VentaRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(VentaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getVentas()
    }

    private fun getVentas(){
        viewModelScope.launch {
            ventaRepository.getAll().collect{ ventas ->
                _uiState.update {
                    it.copy(ventas = ventas)
                }
            }
        }
    }

    fun onEvent(event: VentaUiEvent){
        when(event){
            is VentaUiEvent.ventaIdChanged -> {
                _uiState.update {
                    it.copy(ventaId = event.ventaId)
                }
            }
            is VentaUiEvent.clienteChanged -> {
                _uiState.update {
                    it.copy(
                        cliente = event.cliente,
                        errorCliente = ""
                    )
                }
            }
            is VentaUiEvent.galonesChanged -> {
                if(!_uiState.value.galones.isNullOrEmpty() && !_uiState.value.precio.isNullOrEmpty() && !_uiState.value.descuentoGalon.isNullOrEmpty()){
                    val totalDescontado = _uiState.value.galones.toDouble() * _uiState.value.descuentoGalon.toDouble()
                    val total = (_uiState.value.galones.toDouble() * _uiState.value.precio.toDouble()) - totalDescontado
                    _uiState.update {
                        it.copy(
                            galones = event.galones.toString(),
                            totalDescontado = totalDescontado.toString(),
                            total = total.toString(),
                            errorGalones = ""
                        )
                    }
                }
                else{
                    _uiState.update {
                        it.copy(
                            galones = event.galones.toString(),
                            totalDescontado = "0.0",
                            total = "0.0",
                            errorGalones = ""
                        )
                    }
                }
            }
            is VentaUiEvent.descuentoGalonChanged -> {
                if(!_uiState.value.galones.isNullOrEmpty() && !_uiState.value.precio.isNullOrEmpty() && !_uiState.value.descuentoGalon.isNullOrEmpty()){
                    val totalDescontado = _uiState.value.galones.toDouble() * _uiState.value.descuentoGalon.toDouble()
                    val total = (_uiState.value.galones.toDouble() * _uiState.value.precio.toDouble()) - totalDescontado
                    _uiState.update {
                        it.copy(
                            descuentoGalon = event.descuentoGalon.toString(),
                            totalDescontado = totalDescontado.toString(),
                            total = total.toString(),
                            errorDescuentoGalon = ""
                        )
                    }
                }
                else{
                    _uiState.update {
                        it.copy(
                            descuentoGalon = event.descuentoGalon.toString(),
                            totalDescontado = "0.0",
                            total = "0.0",
                            errorDescuentoGalon = ""
                        )
                    }
                }
            }
            is VentaUiEvent.precioChanged -> {
//                if(_uiState.value.galones.isNotEmpty() && _uiState.value.precio.isNotEmpty() && _uiState.value.descuentoGalon.isNotEmpty()){
//
//                }
//                else{
//                    _uiState.update {
//                        it.copy(
//                            precio = event.precio.toString(),
//                            totalDescontado = "0.0",
//                            total = "0.0",
//                            errorPrecio = ""
//                        )
//                    }
//                }

                val galon = _uiState?.value?.galones?.toDouble() ?: 0.0
                val descuentoGalon = _uiState?.value?.descuentoGalon?.toDouble() ?: 0.0
                val precio = event.precio.toDouble()

                val totalDescontado = galon * descuentoGalon
                val total = (galon * precio) - totalDescontado
                _uiState.update {
                    it.copy(
                        precio = event.precio.toString(),
                        totalDescontado = totalDescontado.toString(),
                        total = total.toString(),
                        errorPrecio = ""
                    )
                }
            }
            is VentaUiEvent.totalDescontadoChanged -> {
                _uiState.update {
                    it.copy(totalDescontado = event.totalDescontado.toString())
                }
            }
            is VentaUiEvent.totalChanged -> {
                _uiState.update {
                    it.copy(total = event.total.toString())
                }
            }
            is VentaUiEvent.selectedVenta -> {
                viewModelScope.launch {
                    if(event.ventaId > 0){
                        val venta = ventaRepository.findVenta(event.ventaId)
                        _uiState.update {
                            it.copy(
                                ventaId = venta?.ventaId,
                                cliente = venta?.cliente.toString(),
                                galones = venta?.galones.toString(),
                                descuentoGalon = venta?.descuentoGalon.toString(),
                                precio = venta?.precio.toString(),
                                totalDescontado = venta?.totalDescontado.toString(),
                                total = venta?.total.toString(),
                            )
                        }
                    }
                }
            }
            VentaUiEvent.Save -> {
                viewModelScope.launch {
                    val cliente = ventaRepository.findCliente(_uiState.value.cliente ?: "")
                    if(_uiState.value.cliente.isNullOrEmpty()){
                        _uiState.update {
                            it.copy(errorCliente = "Este campo no puede estar vacío")
                        }
                    }
                    else if(cliente != null){
                        _uiState.update {
                            it.copy(errorCliente = "Ya existe una venta con este cliente")
                        }
                    }

                    if(_uiState.value.galones == null){
                        _uiState.update {
                            it.copy(errorGalones = "Este campo no puede estar vacío")
                        }
                    }
                    else if(_uiState.value.galones.toDouble() < 0.5 || _uiState.value.galones.toDouble() > 1000){
                        _uiState.update {
                            it.copy(errorGalones = "El minímo de galones es 0.5 y el máximo es de 1,000")
                        }
                    }

                    if(_uiState.value.descuentoGalon == null){
                        _uiState.update {
                            it.copy(errorDescuentoGalon = "Este campo no puede estar vacío")
                        }
                    }
                    else if(_uiState.value.descuentoGalon.toDouble() < 0.01 || _uiState.value.descuentoGalon.toDouble() > 10000){
                        _uiState.update {
                            it.copy(errorDescuentoGalon = "El minímo de descuento por galón es 0.01 y el máximo es de 10,000")
                        }
                    }

                    if(_uiState.value.precio == null){
                        _uiState.update {
                            it.copy(errorPrecio = "Este campo no puede estar vacío")
                        }
                    }
                    else if(_uiState.value.precio.toDouble() < 0.01 || _uiState.value.precio.toDouble() > 10000){
                        _uiState.update {
                            it.copy(errorPrecio = "El minímo de descuento por galón es 0.01 y el máximo es de 10,000")
                        }
                    }

                    if(_uiState.value.errorCliente == "" && _uiState.value.errorGalones == ""
                        && _uiState.value.errorDescuentoGalon == "" && _uiState.value.errorPrecio == ""){
                        ventaRepository.save(_uiState.value.toEntity())
                        _uiState.update {
                            it.copy(success = true)
                        }
                    }
                }
            }
            VentaUiEvent.Delete -> {
                viewModelScope.launch {
                    ventaRepository.delete(_uiState.value.toEntity())
                }
            }
        }
    }

    fun VentaUiState.toEntity() = VentaEntity(
        ventaId = ventaId,
        cliente = cliente,
        galones = galones.toDouble(),
        descuentoGalon = descuentoGalon.toDouble(),
        precio = precio.toDouble(),
        totalDescontado = totalDescontado.toDouble(),
        total = total.toDouble()
    )
}