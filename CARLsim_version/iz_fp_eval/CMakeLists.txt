project(iz_fp_eval)

# Targets  
add_executable(iz_fp_eval
    src/main_iz_fp_eval.cpp
)

# definitions     
if(CARLSIM_NO_CUDA)
    target_compile_definitions(iz_fp_eval PUBLIC
        -D__NO_CUDA__ )
endif()

# Linking    
target_link_libraries(iz_fp_eval PRIVATE
    carlsim
    carlsim-stopwatch
)
target_include_directories(iz_fp_eval PRIVATE
    ../../tools/stopwatch
)
   
        
# Installation   
install(TARGETS iz_fp_eval
    EXPORT carlsim
    RUNTIME 
        DESTINATION $<$<CONFIG:Debug>:debug/>samples 
        COMPONENT iz_fp_eval
        )

#install(FILES run_iz_fp_eval DESTINATION $<$<CONFIG:Debug>:debug/>samples)          