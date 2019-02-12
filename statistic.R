df <- read.delim("/media/ilya/EEA69568A69531D7/py_viz_gnutella/Nodes1.csv", sep=";")
summary(df)
df_100 <- read.delim("/media/ilya/EEA69568A69531D7/py_viz_gnutella/Nodes_top100.csv", sep=";")
hist(df_100$Weight, xlim=c(50,100), xlim=c(50,100),)
