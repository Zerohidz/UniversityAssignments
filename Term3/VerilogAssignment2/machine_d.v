module machine_d(
    input wire x,
    input wire CLK,
    input wire RESET,
    output wire F,
    output wire [2:0] S
);
    
    // A B C x  A+ B+ C+ y
    // 0 0 0 0  0  1  0  0
    // 0 0 0 1  0  0  1  0
    // 0 0 1 0  0  1  1  0
    // 0 0 1 1  0  0  0  0
    // 0 1 0 0  1  0  0  0
    // 0 1 0 1  0  1  1  0
    // 0 1 1 0  1  0  1  0
    // 0 1 1 1  0  1  0  0
    // 1 0 0 0  1  1  0  0
    // 1 0 0 1  1  0  1  0
    // 1 0 1 0  1  1  1  0
    // 1 0 1 1  1  0  0  0
    // 1 1 0 0  1  1  0  1
    // 1 1 0 1  1  1  1  1
    // 1 1 1 0  1  1  1  0
    // 1 1 1 1  1  1  0  0

    // A+ = A + Bx'
    // B+ = Ax' + Bx + B'x'
    // C+ = C'x + Cx'
    // y  = ABC'

    wire x_not;
    not(x_not, x);

    wire B_x_not, A_new;
    and(B_x_not, x_not, S[1]);
    or(A_new, S[2], B_x_not);

    wire B_new, x_nor_B, A_x_not;
    xnor(x_nor_B, x, S[1]);
    and(A_x_not, S[2], x_not);
    or(B_new, x_nor_B, A_x_not);

    wire C_new;
    xor(C_new, S[0], x);

    dff A_ff(.D(A_new), .CLK(CLK), .RESET(RESET), .Q(S[2]));
    dff B_ff(.D(B_new), .CLK(CLK), .RESET(RESET), .Q(S[1]));
    dff C_ff(.D(C_new), .CLK(CLK), .RESET(RESET), .Q(S[0]));

    wire C_not;
    not(C_not, S[0]);
    and(F, S[2], S[1], C_not);

endmodule