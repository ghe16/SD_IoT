from flwr.server import ServerConfig, ServerAppComponents, ClientManager
from flwr.server.strategy import FedAvg

from flwr.common import Context
#from servidor.serverStrategy import strategy
#import servidor.serverStrategy 

def server_fn(context: Context) -> ServerAppComponents:
    """Construimos componentes que definen el comportamiento del servidor

    Se pueden usar los ajustes en `context.run_config' para parametrizar
    la construcción de todos los elementos (por ejemplo la estrategia o
     el número de rondas ) que se devuelven en el objeto ServerAppComponents
     """
    
    # confirugación para 5 rondas de entrenamiento en cada nodo
    config = ServerConfig(num_rounds = 5)
    strategy = FedAvg(
    fraction_fit = 1.0, # muestrea el 100% de los clientes disponibles para entrenamiento
    fraction_evaluate = 0.5, #muestrea el 50% de los clientes para la evaluación
    min_fit_clients=10, # nunca muestreará menos de 10 clientes para training.
    min_evaluate_clients=5, # nunca muestreará menos de 5 para evaluacion
    min_available_clients=10, # espera a que haya al menos 10 clientes disponibles
    )
    client_manager = ClientManager


    return ServerAppComponents(strategy =strategy, config = config, client_manager=client_manager)