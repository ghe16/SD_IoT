from flwr.common import Metrics, Context
from flwr.client import Client
import cliente.clienteApp as clienteApp
import torch
import carga_datasets.load_dataset as LD
import creaRedNeuronal.RN as RN

Device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
partition_id = 0    #inicianos por el nodo 1

def set_partition_id(particion):
    global partition_id 
    partition_id = particion


def client_fn(context: Context) -> Client:
    """creamos un cliente que simula un nodo en la red federada"""
    #cargamos el modelo
    net = RN.Net().to(Device)
    # cada nodo debe recibit un id y una parte del dataset diferente, asi cada nodo se 
    #evaluará en su partición con sus datos únicos 
    #leemos del node_config para obtener la parte de los datos asociadas a cada nodo.
    patition_id = context.node_config["partition_id"]
    trainloader, valloader , _ = LD.load_datasets(partition_id=partition_id)
    return clienteApp.FlowerClient(net,trainloader,valloader).to_client()
    


