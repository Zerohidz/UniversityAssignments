`timescale 1us / 1ns

module ICMS_tb;

    // Inputs
    reg CLK;
    reg RST;
    reg radar_echo;
    reg scan_for_target;
    reg [31:0] jet_speed;
    reg [31:0] max_safe_distance;
    reg [5:0] wind;
    reg thunderstorm;
    reg [1:0] visibility;
    reg signed [7:0] temperature;
	
    // Outputs
    wire radar_pulse_trigger;
    wire [31:0] distance_to_target;
    wire safe_to_engage;
    wire threat_detected;
    wire emergency_landing_alert;
    wire [1:0] ECSU_state;
    wire [1:0] ARTAU_state;
	
    // Instantiate the Unit Under Test (UUT)
    ICMS uut (
        .CLK(CLK),
        .RST(RST),
        .radar_echo(radar_echo),
        .scan_for_target(scan_for_target),
        .jet_speed(jet_speed),
        .max_safe_distance(max_safe_distance),
        .wind(wind),
        .thunderstorm(thunderstorm),
        .visibility(visibility),
        .temperature(temperature),
        .radar_pulse_trigger(radar_pulse_trigger),
        .distance_to_target(distance_to_target),
        .safe_to_engage(safe_to_engage),
        .threat_detected(threat_detected),
        .emergency_landing_alert(emergency_landing_alert),
        .ECSU_state(ECSU_state),
        .ARTAU_state(ARTAU_state)
    );
	
	// Dump to waveform	 file
    initial begin
        $dumpfile("ICMS.wave");
        $dumpvars;
    end
	
    // Clock generation
    initial begin
        CLK = 0;
        forever #50 CLK = ~CLK;
    end
	
    initial begin
        // Initialize Inputs
        RST = 1;
        thunderstorm = 0;
        wind = 0;
        visibility = 2'b00;
        temperature = 0;
		
        // Reset the system
        #100;
        RST = 0;
		#200;
		
		// Stimulus
        thunderstorm = 0; wind = 12; visibility = 2'b00; temperature = 25;
        #300;
		thunderstorm = 0; wind = 5; visibility = 2'b00; temperature = 25;
		#300;
		thunderstorm = 0; wind = 5; visibility = 2'b01; temperature = 25;
		#300;
		thunderstorm = 0; wind = 5; visibility = 2'b00; temperature = 25;
		#300;
		thunderstorm = 0; wind = 5; visibility = 2'b01; temperature = 25;
		#300;
		thunderstorm = 1; wind = 5; visibility = 2'b01; temperature = 25;
        #0;
		#300;
		thunderstorm = 0; wind = 5; visibility = 2'b01; temperature = 25;
		#300;
		wind = 3; visibility = 2'b00; temperature = -5;
		#300;
		visibility = 2'b11; 
		#300;
		wind = 25; visibility = 2'b10;
        #500;
		thunderstorm = 0; wind = 0; visibility = 2'b00; temperature = 0; 
		#200;
		
		// Reset the system
		RST = 1;
		#150;
		RST = 0;
		#300;
		
		thunderstorm = 0; wind = 5; visibility = 2'b01; temperature = 25;
		#300;
		thunderstorm = 0; wind = 15; visibility = 2'b01; 
		#30;
		temperature = 40;
        #0;
		#300;
		thunderstorm = 0; wind = 5; visibility = 2'b01; temperature = 25;
		#300;
		thunderstorm = 0; wind = 25; visibility = 2'b01; temperature = -40;
        #0;
		#600;
    end
	
    initial begin
	  
		// Initialize Inputs
	    radar_echo = 0;
        scan_for_target = 0;
        jet_speed = 0;
        max_safe_distance = 0;
        max_safe_distance = 20000;
        jet_speed = 7000;
		
		// Reset the system
        RST = 1;
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
		
		// Reset the system
        RST=1;
        #100;
        RST=0;
        #100;
		
        $finish; // End the simulation
    end
endmodule