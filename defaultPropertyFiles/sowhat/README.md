# Availability
If you only want to run Beth you can download JAR-File from [BIONF-Repository](https://github.com/BIONF/Beth).
The Source-Code is available at our local working-group repository [AK-Repository](http://gitlab.izn-ffm.intern/dennis/Beth).

The `Beth`-Framework can be used to display phylogenetic trees. It also contains non-parametric tests for phylogenetic hypotheses with `IQ-Tree` and parametric tests with `sowhat`.

# Dependencies

__Beth:__
* [Java-SE RE](http://gitlab.izn-ffm.intern/dennis/Beth)(included in JDK 10), v1.8.
* Any command-line Tool like `Terminal`.

__SOWHAT-Test(modified version for REvolver):__
Phylogenetic programs: 
- [RAxML](https://github.com/stamatak/standard-RAxML), 8.1.20 
- [GARLI](https://code.google.com/p/garli/), v2.01.1067  (optional)
- [Seq-Gen](http://tree.bio.ed.ac.uk/software/seqgen/), v1.3.3
- [ape](http://cran.r-project.org/web/packages/ape/index.html), v3.2
- [RAxMLHPC-PTHREADS](https://sco.h-its.org/exelixis/web/software/raxml/hands_on.html) Run RAxML on many cores

General system tools:
- [Perl](http://www.cpan.org/), which comes with most operating systems
- [R](http://www.r-project.org/)
- The [Statistics::R](http://search.cpan.org/dist/Statistics-R/) Perl module. `Statistics::R` has additional requirements, as described at http://search.cpan.org/dist/Statistics-R/README. Use the `local::lib` option to install `Statistics::R` without `sudo`. Use the boostrap method found at http://search.cpan.org/~haarg/local-lib-2.000004/lib/local/lib.pm for installation information. Once local::lib has been installed, and with R installed,  install the Statistics::R package as you would normally. The use local::lib option must be activated in the program as well.
- The [IPC::Run](http://search.cpan.org/dist/IPC-Run/) Perl module is currently needed for `make test` to work correctly (optional).

We modified the SOWHAT-Test to make sequence-simulation with REvolver. You can download the original version of [SOWHAT](https://github.com/josephryan/sowhat) when following the link.

__REvolver:__

- [HMMER](http://hmmer.org), v3.1b2
- [Java-SE RE](http://gitlab.izn-ffm.intern/dennis/Beth) (included in JDK 10), v1.8.

# Tested Operating Systems
We have tested the Beth-Framework and all Dependencies on Ubuntu 18.04LTS, 16.04LTS and Max OS X 10.13.6.

# Installation

You can install SOWHAT and all the required dependencies listed above on a clean Ubuntu 18.04LTS 
machine with the following commands (executables will be placed in `/usr/local/bin`):

    sudo apt-get update
    sudo apt-get install -y r-base-core cpanminus unzip gcc git
    sudo cpanm Statistics::R
    sudo cpanm JSON
    sudo Rscript -e "install.packages('ape', dependencies = T, repos='http://cran.rstudio.com/')"
    cd ~
    git clone https://github.com/BIONF/Beth
    cd Beth/dist/.beth/sowhat/
    sudo ./build_3rd_party.sh
    perl Makefile.PL
    make
    make test
    sudo make install
    

Note that `build_3rd_party.sh` installs some dependencies from versions that are cached in 
this repository. They may be out of date.

# Start Berth-Framework

Befor you start the Beth-Framework, install all dependencies and run a command-line-Tool like `Terminal`. Check if all dependencies are available in your `$PATH-Variable`.
To Open the Beth-Framework type:

    java -jar /path/to/Beth.jar

Now you can start with open some Treefile or Alignment and create your own hypotheses. Afterwords you can test them.
Have fun!
