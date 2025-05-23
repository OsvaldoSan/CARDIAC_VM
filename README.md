# CARDIAC Virtual Machine: A didactic model of computing

This is a virtual machine designed to explore concurrent and parallel computing using the renowned CARDIAC model, created by David Hagelbarger and Saul Fingerman.

This application was created for the thesis: **CARDIAC: La evolución hacia un modelo concurrente y paralelo**, and is intended to assist any student who may need it.

# How to Use This Application

## Binary Option

To use this version on Windows or Linux, you need to install the most recent version of the Java Runtime Environment. You can find it at[java](https://www.java.com/en/).

Go to the [repository](https://github.com/OsvaldoSan/CARDIAC_VM) and click on the *releases* section.

![Release section](media_g/realese_section.png)

Once you have clicked there, you will see the following image:

![Inside release section](media_g/realse_section_1.png)

Here, you can see two zip files that contain the binary code to execute the program in Windows and GNU/Linux. Download the version appropriate for your operating system.


### Binary Option: Linux

If you download the Linux version, you need to unzip it, and you will see the following folders:

![Folders of Linux version](media_g/folders_linuxv.png)

These folders contain all the necessary information for the program to run. Next, go to the folder named *bin*, where you will see the following files:

![Linux binary files](media_g/linux_binary_files.png)

Then, you need to execute the file named **CVM.sh**. You can run it from the file navigator by double-clicking it, which will show the following dialog. Here, click on "Run."

![Linux option to execute](media_g/executing_linux_file_twoclics.png)

If you encounter any issues with this, another approach is to run it from the terminal using the following command:

![Linux command line](media_g/linux_program_execution_tcl.png)

After that, you will see the program start:

![GUI](media_g/start_window_on_linux.png)

Instructions on how to use the virtual machine can be found in the PDF included in the repository. This PDF refers to the thesis for this project.



### Binary Option: Windows

If you use the Windows option, after downloading and extracting the file **CVM_Windows.zip**, you will see the following folders:

![Folders inside CVM_Windows](media_g/folders_windows.png)

These folders contain all the necessary information for the application to work correctly. To execute the application, go to the *bin* folder, where you will see several files. Look for the file named *CVM*, which is a Windows Batch File, as shown in the image:

![Binary files for Windows](media_g/binary_files_windws.png)

Once you have identified the file, simply double-click on it, and a *CMD* window will open, starting the main *CARDIAC* window.

![Starting CARDIAC in Windows](media_g/starting_cardiac_windows.png)

Instructions on how to use the virtual machine can be found in the PDF included in the repository. This PDF refers to the thesis for this project.


## Create an executable with the source Code

If you would rather create an executable from the source code, you can do so. First, access the repository [CARDIAC_VM](https://github.com/OsvaldoSan/CARDIAC_VM) and clone the code.

Then, simply follow the framework rules of [Apache Maven](https://maven.apache.org/) and edit the *pom* file to your specifications.

Here is an example of how easy it is to deploy the application using Maven in the Linux terminal:

![Maven in Linux](media_g/maven_execution_linux.png)

One of the greatest things about using Maven to deploy the application is that depending on the operating system you use, it will create the corresponding binary file. So if you want a Windows binary, you will need to deploy the app in a Windows file system.

For more information, visit [Apache Maven](https://maven.apache.org/).




