module dff (
    input D,      // Data input
    input CLK,    // Clock input
    input RESET,  // Asynchronous reset, active high
    output reg Q  // Output
);

    always@ (posedge CLK or posedge RESET) begin
        if (RESET) begin
            Q <= 1'b0;
        end
        else begin
            Q <= D;
        end
    end

endmodule