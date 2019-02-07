import networkx as nx
import matplotlib.pyplot as plt

my_graph = nx.Graph()
gml_graph = nx.read_gml("./gml_graph.gml")
nx.draw(gml_graph,with_labels=True)
plt.show()
#plt.savefig("graph1.png", dpi=1000)