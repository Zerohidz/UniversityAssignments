`timescale 1ns/10ps

module full_adder_tb;

    reg A, B, Cin;
    wire S, Cout;

    full_adder f_adder(A, B, Cin, S, Cout);

    initial begin
        $dumpfile("full_adder_tb.vcd");
        $dumpvars;

        A = 0;
        B = 0;
        Cin = 0;
        #10;

        A = 0;
        B = 0;
        Cin = 1;
        #10;
        
        A = 0;
        B = 1;
        Cin = 0;
        #10;
        
        A = 0;
        B = 1;
        Cin = 1;
        #10;
        
        A = 1;
        B = 0;
        Cin = 0;
        #10;
        
        A = 1;
        B = 0;
        Cin = 1;
        #10;
        
        A = 1;
        B = 1;
        Cin = 0;
        #10;
        
        A = 1;
        B = 1;
        Cin = 1;
        #10;
    end

endmodule
