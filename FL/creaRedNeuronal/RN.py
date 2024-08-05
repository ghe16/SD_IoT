import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
import torch 
Device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')


class Net(nn.Module):
    def __init__(self) -> None:
        super(Net, self).__init__()

        self.conv1 = nn.Conv2d(3,6,5)
        self.pool = nn.MaxPool2d(2,2)
        self.conv2 = nn.Conv2d(6,16,5)
        self.fc1 = nn.Linear(16*5*5,120)
        self.fc2 = nn.Linear(120,84)
        self.fc3 = nn.Linear(84,10)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        x = self.pool(F.relu(self.conv1(x)))
        x = self.pool(F.relu(self.conv2(x)))
        x = x.view(-1,16*5*5)
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = self.fc3(x)
        return x


#creamos las funciones de entrenamiento
def train(net, trainloader, epochs: int, verbose = False):
    """ train network with the dataset"""
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(net.parameters())
    net.train()
    for epoch in range(epochs):
        correct, total, epoch_loss = 0, 0, 0
        for batch in trainloader:
            images, labels = batch["img"].to(Device), batch["label"].to(Device)
            optimizer.zero_grad()
            outputs = net(images)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()
            #Metrics
            epoch_loss += loss
            total += labels.size(0)
            correct += (torch.max(outputs.data,1)[1] == labels).sum().item()
            epoch_loss /= len(trainloader.dataset)
            epoch_accuracy = correct / total
            if verbose: print(f"Epoch {epoch+1}: train loss {epoch_loss}, train accuracy {epoch_accuracy}")


def test(net,testloader):
    """Evaluamos la red con el test entero de la red"""
    criterion = nn.CrossEntropyLoss()
    correct, total, loss = 0,0,0
    net.eval()
    with torch.no_grad():
        for batch in testloader:
            images, labels = batch["img"].to(Device), batch["label"].to(Device)
            outputs = net(images)
            loss += criterion(outputs, labels).item()
            _,predicted = torch.max(outputs.data,1)
            total += labels.size(0)
            correct += (predicted == labels).sum().item()
    loss /= len(testloader.dataset)
    accuracy = correct / total
    return loss, accuracy


    
