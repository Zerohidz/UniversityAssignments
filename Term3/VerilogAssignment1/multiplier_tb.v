`timescale 1ns/10ps

module multiplier_tb;

	reg [2:0] A, B;
	wire [5:0] P;

	multiplier multiplier_1(A, B, P);

	integer i, j;
	initial begin
		$dumpfile("multiplier_tb.vcd");
		$dumpvars;

		for (i = 0; i < 8; i = i + 1) begin
			for (j = 0; j < 8; j = j + 1) begin
				A = i;
				B = j;
				#10;
			end
		end
	end
endmodule
