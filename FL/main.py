from collections import OrderedDict
from typing import List, Tuple

import matplotlib.pyplot as plt
import numpy as np
import torch
import torch.nn as nn
import torch.nn.functional as F
from datasets.utils.logging import disable_progress_bar
from torch.utils.data import Dataset, DataLoader
import flwr as fl


from carga_datasets.load_dataset import load_datasets
from graficaDatos.visualizaDatos import visualizaDatos

NUM_OF_CLIENTS = 10
BATCH_SIZE = 32 
dataset_ ="cifar10"
trainloaders = []
valloaders = []

disable_progress_bar()


Device = torch.device('cpu') 
print (
    f"Training on {Device} using Pytorch {torch.__version__} and Flower {fl.__version__}"

)

#Cargamos el dataset. En este caso cifar
trainloaders, valloaders, testloader = load_datasets(dataset_,NUM_OF_CLIENTS,BATCH_SIZE)

#visualizamos los datos de un nodo. Esto lo hacemos tomando datos de entrenamiento y el indice del nodo.
#  el nodo empieza en 0 y va hasta n-1. 

visualizaDatos(trainloaders,0)

#creamos el modelo de la red neuronal que queremos usar. El modelo se compone de un "backbone" y una función de entrenamiento.
