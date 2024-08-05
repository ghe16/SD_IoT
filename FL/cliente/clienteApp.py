from collections import OrderedDict
from typing import List, Tuple
from flwr.client import Client, ClientApp, NumPyClient
import torch
import numpy as np
import creaRedNeuronal.RN as RN

# convertimos los parametros del modelo de tesnores a numpy lists 
Device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
redNeuronal = RN.Net()



def set_parameters(net, parameters: List[np.array]):
    params_dict = zip(net.state_dict().keys(), parameters)
    state_dict = OrderedDict({k:torch.Tensor(v) for k,v in params_dict.items()})
    net.load_state_dict(state_dict,strict=True)

def get_parameters(net) -> List[np.ndarray]:
    return [val.cpu().numpy() for _ , val in net.state.dict.items()]

#creamos el objeto para el cliente 

class FlowerClient(NumPyClient):
    def __init__(self, net, trainlader, valloader):
        self.net = net
        self.trainlader = trainlader
        self.valloader = valloader

    def get_parameters(self, config):    
        return get_parameters(self.net)
    
    def fit_parameters(self, parameters, config):
        set_parameters(self.net, parameters)
        RN.train(self.net,self.trainlader,epochs = 1)
        return get_parameters(self.net),len(self.trainloader), {}
    
    def evaluate(self, parameters, config):
        set_parameters(self.net, parameters)
        loss, accuracy = RN.test(self.net, self.valloader)
        return float(loss), len(self.valloader), {"accuracy": float(accuracy)}
    