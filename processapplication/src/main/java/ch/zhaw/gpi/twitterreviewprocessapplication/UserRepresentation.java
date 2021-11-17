package ch.zhaw.gpi.twitterreviewprocessapplication;

public class UserRepresentation {
    private String firstName;
    private String officialName;
    private String eMail;
    private Links _links;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    public class Links {
        private HomeOrganization homeOrganization;

        public HomeOrganization getHomeOrganization() {
            return homeOrganization;
        }

        public void setHomeOrganization(HomeOrganization homeOrganization) {
            this.homeOrganization = homeOrganization;
        }

        public class HomeOrganization {
            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }
    }
}
