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
                _uiState.update {
                    it.copy(
                        galones = event.galones,
                        errorGalones = "",
                        errorCalculoTotal = "",
                        totalCalculado = false
                    )
                }
            }
            is VentaUiEvent.descuentoGalonChanged -> {
                _uiState.update {
                    it.copy(
                        descuentoGalon = event.descuentoGalon,
                        errorDescuentoGalon = "",
                        errorPrecio = "",
                        errorCalculoTotal = "",
                        totalCalculado = false
                    )
                }
            }
            is VentaUiEvent.precioChanged -> {
                _uiState.update {
                    it.copy(
                        precio = event.precio,
                        errorPrecio = "",
                        errorCalculoTotal = "",
                        totalCalculado = false
                    )
                }
            }
            is VentaUiEvent.totalDescontadoChanged -> {
                _uiState.update {
                    it.copy(totalDescontado = event.totalDescontado)
                }
            }
            is VentaUiEvent.totalChanged -> {
                _uiState.update {
                    it.copy(total = event.total)
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
                    val clienteBuscado = ventaRepository.findCliente(_uiState.value.cliente)

                    if(_uiState.value.cliente.isEmpty()){
                        _uiState.update {
                            it.copy(errorCliente = "Este campo no puede estar vacío")
                        }
                    }
                    else if(clienteBuscado != null && _uiState.value.ventaId != clienteBuscado.ventaId){
                        _uiState.update {
                            it.copy(errorCliente = "Ya existe una venta con este cliente")
                        }
                    }

                    if(!_uiState.value.totalCalculado){
                        _uiState.update {
                            it.copy(errorCalculoTotal = "Aún no ha calculado el total. Asegúrese de pulsar el botón 'Calcular Total'")
                        }
                    }

                    if(_uiState.value.errorCliente == "" && _uiState.value.errorGalones == ""
                        && _uiState.value.errorDescuentoGalon == "" && _uiState.value.errorPrecio == ""
                        && _uiState.value.errorCalculoTotal == "" && _uiState.value.totalCalculado){

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

            VentaUiEvent.CalcularTotal -> {
                if(_uiState.value.galones.isEmpty()){
                    _uiState.update {
                        it.copy(errorGalones = "Este campo no puede estar vacío")
                    }
                }
                else if(_uiState.value.galones.toDouble() < 0.5 || _uiState.value.galones.toDouble() > 1000){
                    _uiState.update {
                        it.copy(errorGalones = "El minímo de galones es 0.5 y el máximo es de 1,000")
                    }
                }

                if(_uiState.value.descuentoGalon.isEmpty()){
                    _uiState.update {
                        it.copy(errorDescuentoGalon = "Este campo no puede estar vacío")
                    }
                }
                else if(_uiState.value.descuentoGalon.toDouble() < 0 || _uiState.value.descuentoGalon.toDouble() > 10000){
                    _uiState.update {
                        it.copy(errorDescuentoGalon = "El minímo de descuento por galón es 0 y el máximo es de 10,000")
                    }
                }

                if(_uiState.value.precio.isEmpty()){
                    _uiState.update {
                        it.copy(errorPrecio = "Este campo no puede estar vacío")
                    }
                }
                else if(_uiState.value.precio.toDouble() < 0.01 || _uiState.value.precio.toDouble() > 10000){
                    _uiState.update {
                        it.copy(errorPrecio = "El precio minímo es de 0.01 y el máximo es de 10,000")
                    }
                }

                else if (_uiState.value.precio.toDouble()  <= _uiState.value.descuentoGalon.toDouble()) {
                    _uiState.update {
                        it.copy(errorPrecio = "El precio no puede ser menor o igual que el descuento")
                    }
                }

                if(_uiState.value.errorGalones == "" && _uiState.value.errorDescuentoGalon == ""
                    && _uiState.value.errorPrecio == ""){

                    val totalDescontado = _uiState.value.galones.toDouble() * _uiState.value.descuentoGalon.toDouble()
                    val total = (_uiState.value.galones.toDouble() * _uiState.value.precio.toDouble()) - totalDescontado

                    _uiState.update {
                        it.copy(
                            totalDescontado = totalDescontado.toString(),
                            total = total.toString(),
                            totalCalculado = true,
                            errorCalculoTotal = ""
                        )
                    }
                }
            }
        }
    }

    fun VentaUiState.toEntity() = VentaEntity(
        ventaId = ventaId,
        cliente = cliente,
        galones = galones.toDoubleOrNull() ?: 0.0,
        descuentoGalon = descuentoGalon.toDoubleOrNull() ?: 0.0,
        precio = precio.toDoubleOrNull() ?: 0.0,
        totalDescontado = totalDescontado.toDoubleOrNull() ?: 0.0,
        total = total.toDoubleOrNull() ?: 0.0
    )
}