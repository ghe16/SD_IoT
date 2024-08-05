from flwr.server.strategy import FedAvg
#Crear estrategia para el servidor usando la función FedAvg

strategy = FedAvg(
    fraction_fit = 1.0, # muestrea el 100% de los clientes disponibles para entrenamiento
    fraction_evaluate = 0.5, #muestrea el 50% de los clientes para la evaluación
    min_fit_clients=2, # nunca muestreará menos de 10 clientes para training.
    min_evaluate_clients=2, # nunca muestreará menos de 5 para evaluacion
    min_available_clients=2, # espera a que haya al menos 10 clientes disponibles

)
