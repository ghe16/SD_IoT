from datasets.load_dataset import load_dataset



NUM_OF_CLIENTS = 10
BATCH_SIZE = 32 


trainloaders, valloaders, testloader = load_dataset("cifar10",NUM_OF_CLIENTS)
