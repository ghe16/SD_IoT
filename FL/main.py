
import matplotlib.pyplot as plt
import numpy as np
import torch
from datasets.utils.logging import disable_progress_bar
from torch.utils.data import Dataset, DataLoader

import flwr as fl
import flwr

from flwr.client import Client, ClientApp, NumPyClient 
from flwr.server import ServerApp, ServerConfig, ServerAppComponents, ClientManager
from flwr.simulation import run_simulation


from carga_datasets.load_dataset import load_datasets
from graficaDatos.visualizaDatos import visualizaDatos
import  creaRedNeuronal.RN  as RN

NUM_OF_CLIENTS = 3
BATCH_SIZE = 32 
dataset_ ="cifar10"
trainloaders = []
valloaders = []

disable_progress_bar()


Device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

print (
    f"Training on {Device} using Pytorch {torch.__version__} and Flower {fl.__version__}"

)

#Cargamos el dataset. En este caso cifar
trainloaders, valloaders, testloader = load_datasets(dataset_,NUM_OF_CLIENTS,BATCH_SIZE)

#visualizamos los datos de un nodo. Esto lo hacemos tomando datos de entrenamiento y el indice del nodo.
#  el nodo empieza en 0 y va hasta n-1. 

visualizaDatos(trainloaders,0)

#creamos el modelo de la red neuronal que queremos usar. El modelo se compone de un "backbone" y una función de entrenamiento.
redNeuronal = RN.Net()


#################################################################3
###################################################################
"""ENTRENAMIENTO CENTRALIZADO"""
###############################################################33
##############################################################3
#hacemos el entrenamiento para un nodo. En este caso, el nodo 0. 

trainloader = trainloaders[0]
valloader = valloaders[0]
net = RN.Net().to(Device)

for epoch in range(5):
    RN.train(net,trainloader,1)
    loss,accuracy = RN.test(net,valloader)
    print(f" Epoch {epoch+1}: validation loss: {loss} accuracy loss {accuracy}")

#probamos la red entrenada
loss, accuracy = RN.test(net,valloader)
print(f"Final set performance: \n\tloss: {loss}, accuracy: {accuracy}")



#################################################################3
###################################################################
"""ENTRENAMIENTO FEDERADO """
###############################################################33
##############################################################3



#creamos la app de cliente
from cliente.client_fn import  client_fn, set_partition_id


client_id = set_partition_id(0)    #iniciamos con el primer nodo. 
client = ClientApp(client_fn = client_fn) 


#creamos la app para servidor
from servidor import servidorApp
server = ServerApp(server = servidorApp.server_fn)


#configuramos los parametros para la ejecucion
if Device == 'cuda':
    backend_config = {"client_resources": {"num_cpus": 1, "num_gpus": 1.0}}
else:
    backend_config = {"client_resources": {"num_cpus": 1, "num_gpus":0.0}}



#ejecutamos la simulacion

run_simulation(
    server_app=server,
    client_app=client,
    num_supernodes=NUM_OF_CLIENTS,
    backend_config=backend_config,

)

#####  Sacamos las metricas: accuracy
##### Hasta aquí, todo bien.... Pero ¿ya hemos acabado?
### No, entrenamos nuestro modelo, pero no podemos saber si está convergiendo, si el accuracy mejora, o el precision o el F1-score. 
### Por supuesto, aqui no podemos usar los mismos valores que en el modelo centralizado.... Tenemos N_CLIENTS= 10. 
## FL no puede hacer esto automáticamente... pero si puede hacer para cada nodo la operación que querramos. 
##Es decir, si proveemos la ecueción o formula ... FL puede calcularlo para todas los nodos. 
## esto se hace con las funciones fit_metrics_aggregation_fn    y     evaluate_metrics_aggregation_fn 


