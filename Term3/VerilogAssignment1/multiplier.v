`timescale 1ns/10ps

module multiplier (
    input [2:0] A, B,
    output [5:0] P
);
	
    and(P[0], A[0], B[0]);

    wire a1, a2, a3, a4, a5;
    and(a1, A[1], B[0]);
    and(a2, A[2], B[0]);
    and(a3, A[0], B[1]);
    and(a4, A[1], B[1]);
    and(a5, A[2], B[1]);

    wire c1, c2;
    wire s1;
    half_adder h_adder_1(a3, a1, P[1], c1);
    half_adder h_adder_2(a4, a2, s1, c2);

    wire a6, a7, a8;
    and(a6, A[0], B[2]);
    and(a7, A[1], B[2]);
    and(a8, A[2], B[2]);

    wire c3, c4;
    wire s2;
    full_adder f_adder_1(a6, s1, c1, P[2], c3);
    full_adder f_adder_2(a7, a5, c2, s2, c4);

    wire c5;
    half_adder h_adder_3(s2, c3, P[3], c5);

    full_adder f_adder_3(a8, c4, c5, P[4], P[5]);
endmodule
