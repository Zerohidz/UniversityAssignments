`timescale 1ns/10ps

module full_adder(
    input A, B, Cin,
    output S, Cout
);
    wire S1, Cout1, Cout2;

    half_adder h_adder_1(A, B, S1, Cout1);
    half_adder h_adder_2(S1, Cin, S, Cout2);

    or(Cout, Cout1, Cout2);

endmodule
