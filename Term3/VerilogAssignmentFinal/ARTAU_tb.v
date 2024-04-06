`timescale 1us / 1ns

module ARTAU_tb;

    // Inputs
    reg radar_echo;
    reg scan_for_target;
    reg [31:0] jet_speed;
    reg [31:0] max_safe_distance;
    reg RST;
    reg CLK;

    // Outputs
    wire radar_pulse_trigger;
    wire [31:0] distance_to_target;
    wire threat_detected;
    wire [1:0] ARTAU_state;

    // Clock generation
    initial begin
        CLK = 0;
        forever #50 CLK = ~CLK; 
    end

    // Instantiate the Unit Under Test (UUT)
    ARTAU uut(
        .radar_echo(radar_echo),
        .scan_for_target(scan_for_target),
        .jet_speed(jet_speed),
        .max_safe_distance(max_safe_distance),
        .RST(RST),
        .CLK(CLK),
        .radar_pulse_trigger(radar_pulse_trigger),
        .distance_to_target(distance_to_target),
        .threat_detected(threat_detected),
        .ARTAU_state(ARTAU_state)
    );

    // Dump to waveform file
    initial begin
        $dumpfile("ARTAU.wave");
        $dumpvars;
    end

    initial begin

        // Initialize Inputs
        RST = 1;
        radar_echo = 0;
        scan_for_target = 0;
        jet_speed = 0;
        max_safe_distance = 0;
        max_safe_distance = 20000;
        jet_speed = 7000;
        
        // Reset the system
        #100;
        RST = 0;
        #200;

        // Stimulus
        scan_for_target = 1;
        #25;
        scan_for_target = 0;
        #25;
        #250; 
        #50;
        #50;
        radar_echo = 1;
        #2;
        radar_echo = 0;
        #350;
        radar_echo = 1;
        #2;
        radar_echo = 0;
        #98;
        #3100;
        scan_for_target = 1;
        #50;
        scan_for_target = 0;
        #400;
        radar_echo = 1;
        #10;
        radar_echo = 0;
        #500;
        radar_echo = 1;
        #10;
        radar_echo = 0;
        #300;
        scan_for_target = 1;
        #150;
        scan_for_target = 0;
        #2300;
        scan_for_target = 1;
        #50;
        scan_for_target = 0;
        #600;
        RST=1;
        #100;
        RST=0;
        #100;

        // End the simulation
        $finish;
    end

endmodule