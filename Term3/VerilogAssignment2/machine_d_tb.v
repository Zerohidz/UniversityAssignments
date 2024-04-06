`timescale 1ns / 1ps

module machine_d_tb;
    
    reg x;
    reg CLK;
    reg RESET;
    wire F;
    wire [2:0] S;
    
    machine_d machine_d_inst(
        .x(x),
        .CLK(CLK),
        .RESET(RESET),
        .F(F),
        .S(S)
    );
    
    integer i;
    initial begin
        $dumpfile("machine_d_tb.vcd");
        $dumpvars;
        x = 0;
        RESET = 1;
        #5;
        RESET = 0;
        #4;
        for (i = 0; i < 3 ;i = i + 1) begin
            x = 1;
            #40;
            x = 0;
            #20;
        end
        #20;
        x = 1;
        #20;
        x = 0;
        #20;
        x = 1;
        #20;
        RESET = 1;
        #5;
        RESET = 0;
        #4;
        #20;
        x = 0;
        #80;
        x = 1;
        #20;
        x = 1; // repetition
    end

    integer j;
    initial begin
        CLK = 0;
        for (j = 0; j < 40; j = j + 1) begin
            #10 CLK = ~CLK;
        end
    end

endmodule