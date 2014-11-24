network-motif
=============

Network Motif analysis using ESU algorithm (Java)

## Test Drive

#### Prerequisites

- `labelg` program compiled for your computer. If you're working through the UWB Linux Lab, the provided executable will suffice. Otherwise you will need to compile and include your own: replace `labelg` with an identically named executable and change the mode to allow execution (`chmod +x labelg`). The source is available at http://cs.anu.edu.au/~bdm/nauty/ (to build, `bash configure && make all`).
- Mac/Linux platform, if you want to use the shell scripts. If you want to run this in Windows, you'll probably need to update the `labelg` program name (`programName` in `Labeler.java`).

#### Execution

Compile the code, then run the program on the master node:
``` bash
$ ./compile.sh

$ ./run.sh datafile motif [--show-results]

# examples:

# motif size of 3, print canonical label counts:
$ ./run.sh data/Scere20131031CR.txt 3 --show-results

# motif size of 4, don't print results
$ ./run.sh data/test05 4
```

## Data File Format

Text file, with each line containing a "from" node and a "to" node, as indicated by the string. These are interpreted to be undirected connections, so the ordering does not actually matter.

Example:

```
a b
b c
b d
d e
```

This represents the following graph:

```
 ___        ___        ___
| a | ---- | b | ---- | c |
 ---        ---        ---
             |
             |
            ___        ___
           | d | ---- | e |
            ---        ---
```
