JC           = javac
J            = java
SRCPATH      = ./src/
BINPATH	     = ./bin/
LIBPATH      = ./lib/
GRBPATH      = /util/academic/gurobi/gurobi702/linux64/lib/gurobi.jar
INSTANCE     = 10
DISTRIBUTION = 0
all:
	$(JC) -cp $(GRBPATH):$(LIBPATH)* -d $(BINPATH) $(find ./src -name '*.java')

clean:
	rm $(BINPATH)*.class

run:
	$(J) -cp $(GRBPATH):$(LIBPATH)*:$(SRCPATH):$(BINPATH) Solution $(INSTANCE) $(DISTRIBUTION)
