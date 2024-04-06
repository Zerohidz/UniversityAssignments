`timescale 1ns/10ps

module half_adder_tb;

	reg A, B;
	wire S, C;

	half_adder h_adder(A, B, S, C);

	initial begin
		$dumpfile("half_adder_tb.vcd");
		$dumpvars;

		A = 0;
		B = 0;
		#10;

		A = 0;
		B = 1;
		#10;

		A = 1;
		B = 0;
		#10;

		A = 1;
		B = 1;
		#10;
	end

endmodule
