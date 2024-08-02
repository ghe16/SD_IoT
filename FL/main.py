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


NUM_OF_CLIENTS = 10
BATCH_SIZE = 32 
dataset_ ="cifar10"
trainloaders = []
valloaders = []

Device = torch.device('cpu') 
print (
    f"Training on {Device} using Pytorch {torch.__version__} and Flower {fl.__version__}"

)

trainloaders, valloaders, testloader = load_datasets(dataset_,NUM_OF_CLIENTS,BATCH_SIZE)
