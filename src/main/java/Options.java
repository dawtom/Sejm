/**
 * Created by Dawid Tomasiewicz on 23.12.16.
 */
public class Options {
    private Integer parliamentTerm;
    private Integer optionNumber;
    private String memberOfParliamentFirstName;
    private String memberOfParliamentLastName;


    public Integer getParliamentTerm() {
        return parliamentTerm;
    }


    public void setParliamentTerm(Integer parliamentTerm) {
        this.parliamentTerm = parliamentTerm;
    }

    public Integer getOptionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(Integer optionNumber) {
        this.optionNumber = optionNumber;
    }

    public String getMemberOfParliamentFirstName() {
        return memberOfParliamentFirstName;
    }

    public void setMemberOfParliamentFirstName(String memberOfParliamentFirstName) {
        this.memberOfParliamentFirstName = memberOfParliamentFirstName;
    }

    public String getMemberOfParliamentLastName() {
        return memberOfParliamentLastName;
    }

    public void setMemberOfParliamentLastName(String memberOfParliamentLastName) {
        this.memberOfParliamentLastName = memberOfParliamentLastName;
    }

    @Override
    public String toString(){
        return "parliamentTerm: " + this.parliamentTerm +
                "\noptionNumber: " + this.optionNumber +
                "\nFirstName: " + this.memberOfParliamentFirstName +
                "\nLastName: " + this.memberOfParliamentLastName;
    }
}
