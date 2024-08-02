import matplotlib.pyplot as plt




#visualization of samples for node 0 
def visualizaDatos(datos_list: list,nodo: int):
    batch = next(iter(datos_list[nodo]))
    images, labels = batch["img"], batch["label"]
    # Reshape and convert images to a NumPy array
    # matplotlib requires images with the shape (height, width, 3)
    images = images.permute(0,2,3,1).numpy()
    #Denormalize
    images = images / 2  + .5

    #create a figure and a grid of subplots. 
    fig, axs = plt.subplots(4, 8, figsize=(12,6))

    #loop over images and plot them

    for i, ax in enumerate(axs.flat):
        ax.imshow(images[i])
        ax.set_title(datos_list[nodo].dataset.features["label"].int2str([labels[i]])[nodo])
        ax.axis("off")

    #show the plot
    fig.tight_layout()
    plt.show()