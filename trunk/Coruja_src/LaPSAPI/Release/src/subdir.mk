################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/RecoResult.cpp \
../src/SREngine.cpp 

OBJS += \
./src/RecoResult.o \
./src/SREngine.o 

CPP_DEPS += \
./src/RecoResult.d \
./src/SREngine.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -I"/home/pedro/workspace/LaPSAPI/julius-4.1.4/libsent/include" -I"/home/pedro/workspace/LaPSAPI/include" -I"/home/pedro/workspace/LaPSAPI/julius-4.1.4/libjulius/include" -O3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


