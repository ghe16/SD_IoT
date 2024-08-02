#from datasets.utils.logging import disable_progress_bar
from torch.utils.data import Dataset, DataLoader

from flwr_datasets import FederatedDataset 
import torchvision.transforms as transforms 


def load_datasets(dataset_,NUM_OF_CLIENTS,BATCH_SIZE):
    fds = FederatedDataset(dataset="cifar10", partitioners={"train":NUM_OF_CLIENTS})
    def apply_transforms(batch):
        # Instead of passing transforms to dataset_(..., transform=transform)
        # we will use this function to dataset.with_transform(apply_transforms)
        # The transforms object is exactly the same

        transform = transforms.Compose(
            [
                transforms.ToTensor(),
                transforms.Normalize((0.5,0.5,0.5),(0.5,0.5,0.5))
                                    #mean,std

            ]
        )
        batch["img"] = [transform(img) for img in batch["img"]]
        return batch
    
    #create train/val for each partition and wrap it into Dataloader

    trainloaders = []
    valloaders = []

    for partition_id in range(NUM_OF_CLIENTS):
        partition = fds.load_partition(partition_id,"train")
        partition = partition.with_transform(apply_transforms)
        partition = partition.train_test_split(train_size=0.8, seed=42)
        trainloaders.append(DataLoader(partition["train"],batch_size=BATCH_SIZE))
        valloaders.append(DataLoader(partition["test"],batch_size=BATCH_SIZE))
    testset = fds.load_split("test").with_transform(apply_transforms)
    testloader = DataLoader(testset, batch_size=BATCH_SIZE)
    return trainloaders, valloaders, testloader