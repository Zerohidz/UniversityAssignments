import java.util.ArrayList;

public class MemberManager {
    private static ArrayList<Member> students = new ArrayList<Member>();
    private static ArrayList<Member> academics = new ArrayList<Member>();
    private static ArrayList<Integer> existingIds = new ArrayList<Integer>();

    // addMember
    public static void handleAddMember(String[] command) {
        if (command.length < 1 || "SA".contains(command[0]) == false || existingIds.size() >= 999999) {
            System.out.println(Messages.GeneralError);
            return;
        }

        int id = existingIds.size() == 0 ? 1 : existingIds.get(existingIds.size() - 1) + 1;
        MemberType memberType = command[0].equals("S") ? MemberType.Student : MemberType.Academic;
        if (memberType == MemberType.Student)
            students.add(new Member(id, memberType));
        else
            academics.add(new Member(id, memberType));

        existingIds.add(id);

        System.out.println(String.format("Created new member: %s [id: %d]", memberType.toString(), id));
    }

    public static boolean doesMemberExist(int memberId) {
        return existingIds.contains(memberId);
    }

    public static Member getMemberById(int memberId) {
        if (!doesMemberExist(memberId))
            return null;

        for (Member member : students)
            if (member.getId() == memberId)
                return member;

        for (Member member : academics)
            if (member.getId() == memberId)
                return member;

        return null;
    }

    public static ArrayList<Member> getStudents() {
        return students;
    }

    public static ArrayList<Member> getAcademics() {
        return academics;
    }
}
