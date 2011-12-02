/**
 * 
 */
package fr.explorimmo.poc.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import fr.explorimmo.poc.domain.validation.Create;
import fr.explorimmo.poc.domain.validation.Delete;
import fr.explorimmo.poc.domain.validation.Update;

/**
 * @author louis.gueye@gmail.com
 */
@Entity
@Table(name = Advert.TABLE_NAME)
@XmlRootElement
public class Advert extends AbstractEntity {

    public static final String TABLE_NAME = "advert";

    public static final String COLUMN_NAME_ID = "advert_id";
    public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";

    public static final int CONSTRAINT_NAME_MAX_SIZE = 50;
    public static final int CONSTRAINT_DESCRIPTION_MAX_SIZE = 200;
    public static final int CONSTRAINT_EMAIL_MAX_SIZE = 100;
    public static final int CONSTRAINT_PHONE_NUMBER_MAX_SIZE = 20;
    public static final int CONSTRAINT_REFERENCE_MAX_SIZE = 100;

    /**
	 * 
	 */
    private static final long serialVersionUID = -5952533696555432772L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = Advert.COLUMN_NAME_ID)
    @NotNull(message = "{advert.id.required}", groups = { Update.class, Delete.class })
    private Long id;

    @NotEmpty(message = "{advert.name.required}", groups = { Create.class, Update.class })
    @Size(max = Advert.CONSTRAINT_NAME_MAX_SIZE, message = "{advert.name.max.size}", groups = { Create.class,
            Update.class })
    private String name;

    @Size(max = Advert.CONSTRAINT_DESCRIPTION_MAX_SIZE, message = "{advert.description.max.size}", groups = {
            Create.class, Update.class })
    private String description;

    @NotNull(message = "{advert.email.required}", groups = { Create.class, Update.class })
    @Email(message = "{advert.email.valid.format.required}", groups = { Create.class, Update.class })
    @Size(max = Advert.CONSTRAINT_EMAIL_MAX_SIZE, message = "{advert.email.max.size}", groups = { Create.class,
            Update.class })
    private String email;

    @Column(name = Advert.COLUMN_NAME_PHONE_NUMBER)
    @NotEmpty(message = "{advert.phoneNumber.required}", groups = { Create.class, Update.class })
    @Size(max = Advert.CONSTRAINT_PHONE_NUMBER_MAX_SIZE, message = "{advert.phoneNumber.max.size}", groups = {
            Create.class, Update.class })
    private String phoneNumber;

    @NotEmpty(message = "{advert.reference.required}", groups = { Create.class, Update.class })
    @Size(max = Advert.CONSTRAINT_REFERENCE_MAX_SIZE, message = "{advert.reference.max.size}", groups = { Create.class,
            Update.class })
    private String reference;

    @Valid
    @NotNull(message = "{advert.address.required}", groups = { Create.class, Update.class })
    private Address address;

    /**
	 * 
	 */
    public Advert() {
        super();
        setAddress(new Address());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Advert other = (Advert) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Address getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

}
