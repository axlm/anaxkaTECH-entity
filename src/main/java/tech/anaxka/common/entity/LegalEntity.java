// $Id$

/*
 * \u00A9 2012, 4axka (Pty) Ltd.  All rights reserved.
 *
 * The content of LegalEntity.java is strictly CONFIDENTIAL.
 *
 * It may not be viewed as a whole, or in part by any unauthorised party unless
 * explicit permission has been granted by an authorised 4axka representative.
 *
 * It may not be reproduced as a whole, or in part by any means unless explicit
 * permission has been granted by an authorised 4axka representative.
 */
package tech.anaxka.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import tech.anaxka.util.functor.Modifier;
import tech.anaxka.util.functor.Predicate;

import static java.util.Collections.unmodifiableList;
import static tech.anaxka.util.lang.ToString.toStringBuilder;

/**
 * @author <a href="mailto:axl.mattheus@4axka.net">4axka (Pty) Ltd</a>
 */
@XmlRootElement(name = "legalEntity")
@XmlType(name = "LegalEntity")
@XmlSeeAlso({Person.class})
@MappedSuperclass
public abstract class LegalEntity implements Serializable {

    /**
     * Determines if a de-serialised file is compatible with this class.
     * <p>
     * Maintainers <strong>MUST</strong> change this value if and only if the new version of this
     * class is not compatible with the previous version. It is not necessary to include in first
     * version of the class, but included here as a reminder of its importance.
     * <p>
     * @see <a href="http://bit.ly/aDUV5">Java Object Serialization Specification</a>.
     */
    @XmlTransient
    @Transient
    private static final long serialVersionUID = 2724081153382480314L;

    @XmlTransient
    @Id
    @TableGenerator(
            name = "entity_id_generator",
            table = "PRIMARY_KEYS",
            pkColumnName = "GENERATOR",
            pkColumnValue = "entity_id",
            valueColumnName = "VALUE")
    @GeneratedValue(generator = "entity_id_generator")
    @Column(name = "ID")
    private Long __id;

    @XmlTransient
    @Version
    @Column(name = "VERSION_LOCK")
    private Integer __version;

    @XmlElementWrapper(name = "emailAddresses")
    @XmlElement(name = "emailAddress")
    @OneToMany(
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @OrderBy("__type ASC")
    @JoinColumn(name = "ENTITY_FK", referencedColumnName = "ID")
    private final Set<EmailAddress> __emailAddresses = new ConcurrentSkipListSet<>();

    @XmlElementWrapper(name = "telephoneNumbers")
    @XmlElement(name = "telephoneNumber")
    @OneToMany(
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @OrderBy("__type ASC")
    @JoinColumn(name = "ENTITY_FK", referencedColumnName = "ID")
    private final Set<TelephoneNumber> __telephoneNumbers = new ConcurrentSkipListSet<>();

    @XmlElementWrapper(name = "addresses")
    @XmlElement(name = "address")
    @OneToMany(
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    @OrderBy("__type ASC")
    @JoinColumn(name = "ENTITY_FK", referencedColumnName = "ID")
    private final Set<Address> __addresses = new ConcurrentSkipListSet<>();

    /**
     * Default constructor.
     * <p>
     * This constructor is supplied to conform to the JavaBeans 1.01 Specification. It
     * <strong>MUST NOT</strong> be invoked directly.
     * <p>
     * @see <a href="http://bit.ly/BddaX">JavaBeans 1.01 Specification</a>.
     */
    public LegalEntity() {
        super();
    }

    /**
     * Instance variable constructor. Initialise {@code this} instance with the specified arguments.
     * <i>For state specifications see the see also section</i>.
     * <p>
     * @param emailAddresses  see {@link LegalEntity#getEmailAddresses() email addresses}.
     * @param numbers         see {@link LegalEntity#getTelephoneNumbers() telephone numbers}.
     * @param addresses       see {@link LegalEntity#getAddresses() addresses}.
     */
    public LegalEntity(
            final Iterable<EmailAddress> emailAddresses,
            final Iterable<TelephoneNumber> numbers,
            final Iterable<Address> addresses) {
        this();
        addEmailAddresses(emailAddresses);
        addTelephoneNumbers(numbers);
        addAddresses(addresses);
    }

    /**
     * Copy constructor. <i>For state specifications see the see also section</i>.
     * <p>
     * @param template Uses template as template to initialise
     *                 {@linkplain LegalEntity {@code this}}.
     * <p>
     * @see super
     */
    public LegalEntity(final LegalEntity template) {
        this(
                template.getEmailAddresses(),
                template.getTelephoneNumbers(),
                template.getAddresses());
    }

    /**
     * Obvious.
     * <p>
     * @return The value of {@code this} instance's {@linkplain #__id id}.
     */
    public final Long getId() {
        return __id;
    }

    void setId(final Long id) {
        __id = id;
    }

    /**
     * Obvious.
     * <p>
     * @return The value of {@code this} instance's {@linkplain #__version version}.
     */
    public final Integer getVersion() {
        return __version;
    }

    /**
     * Obvious.
     * <p>
     * @return All the {@linkplain #__emailAddresses email addresses} associated with {@code this}
     *         {@linkplain LegalEntity legal entity}.
     * <p>
     * <b>NOTE:</b> This operation returns a defensive copy of each {@link EmailAddress} associated with
     * {@code this} {@linkplain LegalEntity legal entity}.
     */
    public final Iterable<EmailAddress> getEmailAddresses() {
        // make defensive copies for thread safety
        final List<EmailAddress> result_ = new ArrayList<>();

        for (EmailAddress ea_ : __emailAddresses) {
            result_.add(new EmailAddress(ea_));
        }

        return unmodifiableList(result_);
    }

    /**
     * Obvious.
     * <p>
     * @param address {@link EmailAddress Email address} to associate with {@link LegalEntity this}
     *                legal entity.
     * <p>
     * @return {@code true} if the {@linkplain EmailAddress email address} was successfully added.
     */
    public final boolean addEmailAddress(final EmailAddress address) {
        return __emailAddresses.add(address);
    }

    /**
     * Obvious.
     * <p>
     * @param addresses A {@linkplain Iterable collection} of
     *                  {@linkplain EmailAddress email addresses} to add to {@code this}
     *                  {@link LegalEntity}.
     * <p>
     * @return A {@linkplain Iterable sequence} of {@link EmailAddress addresses} added to
     *         {@code this}.
     */
    public final Iterable<EmailAddress> addEmailAddresses(final Iterable<EmailAddress> addresses) {
        final List<EmailAddress> result_ = new ArrayList<>();

        for (final EmailAddress ea_ : addresses) {
            if (addEmailAddress(ea_)) {
                result_.add(ea_);
            }
        }

        return result_;
    }

    /**
     * Obvious.
     * <p>
     * @param addresses {@code Array} of {@linkplain EmailAddress email addresses} to add to
     *                  {@code this}.
     * <p>
     * @return The {@link Address addresses} added to {@code this}.
     */
    public final Iterable<EmailAddress> addEmailAddresses(final EmailAddress... addresses) {
        final List<EmailAddress> result_ = new ArrayList<>();

        for (final EmailAddress ea_ : addresses) {
            if (addEmailAddress(ea_)) {
                result_.add(ea_);
            }
        }

        return result_;
    }

    /**
     * Obvious.
     * <p>
     * @param address {@link EmailAddress} to remove from {@code this} {@linkplain #__emailAddresses
     *  		email addresses}.
     * <p>
     * @return {@code true} if the {@linkplain EmailAddress email address} was removed.
     */
    public final boolean removeEmailAddress(final EmailAddress address) {
        return __emailAddresses.remove(address);
    }

    /**
     * Removes a {@code EmailAddress} matched by the supplied {@linkplain Predicate predicate}.
     * <p>
     * @param predicate <p>
     * @return The removed {@linkplain EmailAddress addresses}.
     */
    public final Iterable<EmailAddress> removeEmailAddresses(
            final Predicate<EmailAddress, IllegalStateException> predicate) {
        // how should a predicate behave? should it throw any exceptions?
        final List<EmailAddress> result_ = new ArrayList<>();

        for (EmailAddress ea_ : __emailAddresses) {
            if (predicate.match(ea_)) {
                if (removeEmailAddress(ea_)) {
                    result_.add(ea_);
                }
            }
        }

        return result_;
    }

    /**
     *
     * @param predicate Matcher for search.
     * <p>
     * @return {@linkplain EmailAddress Email addresses} found by matching {@link Predicate}.
     */
    public Iterable<EmailAddress> findEmailAddresses(
            final Predicate<EmailAddress, IllegalStateException> predicate) {
        final List<EmailAddress> result_ = new ArrayList<>();

        for (EmailAddress ea_ : __emailAddresses) {
            if (predicate.match(ea_)) {
                result_.add(ea_);
            }
        }

        return result_;
    }

    /**
     *
     * @param predicate Matcher for search.
     * @param modifier Modifier.
     * <p>
     * @return The modified {@linkplain EmailAddress email addresses}.
     */
    public Iterable<EmailAddress> modifyEmailAddresses(
            final Predicate<EmailAddress, IllegalStateException> predicate,
            final Modifier<EmailAddress, IllegalStateException> modifier) {
        final List<EmailAddress> result_ = new ArrayList<>();

        for (EmailAddress ea_ : __emailAddresses) {
            if (predicate.match(ea_)) {
                if (removeEmailAddress(ea_)) {
                    final EmailAddress mea_ = modifier.modify(ea_);
                    if (addEmailAddress(mea_)) {
                        result_.add(mea_);
                    }
                }
            }
        }

        return result_;
    }

    public final Iterable<TelephoneNumber> getTelephoneNumbers() {
        // make defensive copies for thread safety
        final List<TelephoneNumber> result_ = new ArrayList<>();

        for (TelephoneNumber t_ : __telephoneNumbers) {
            result_.add(new TelephoneNumber(t_));
        }

        return unmodifiableList(result_);
    }

    public final boolean addTelephoneNumber(final TelephoneNumber number) {
        return __telephoneNumbers.add(number);
    }

    public final Iterable<TelephoneNumber> addTelephoneNumbers(
            final Iterable<TelephoneNumber> numbers) {
        final List<TelephoneNumber> result_ = new ArrayList<>();

        for (final TelephoneNumber t_ : numbers) {
            if (addTelephoneNumber(t_)) {
                result_.add(t_);
            }
        }

        return result_;
    }

    public final Iterable<TelephoneNumber> addTelephoneNumbers(final TelephoneNumber... numbers) {
        final List<TelephoneNumber> result_ = new ArrayList<>();

        for (final TelephoneNumber t_ : numbers) {
            if (addTelephoneNumber(t_)) {
                result_.add(t_);
            }
        }

        return result_;
    }

    public final boolean removeTelephoneNumber(final TelephoneNumber number) {
        return __telephoneNumbers.remove(number);
    }

    public final Iterable<TelephoneNumber> removeTelephoneNumbers(
            final Predicate<TelephoneNumber, IllegalStateException> predicate) {
        // how should a predicate behave? should it throw any exceptions?
        final List<TelephoneNumber> result_ = new ArrayList<>();

        for (TelephoneNumber t_ : __telephoneNumbers) {
            if (predicate.match(t_)) {
                if (removeTelephoneNumber(t_)) {
                    result_.add(t_);
                }
            }
        }

        return result_;
    }

    public Iterable<TelephoneNumber> findTelephoneNumbers(
            final Predicate<TelephoneNumber, IllegalStateException> predicate) {
        final List<TelephoneNumber> result_ = new ArrayList<>();

        for (TelephoneNumber t_ : __telephoneNumbers) {
            if (predicate.match(t_)) {
                result_.add(t_);
            }
        }

        return result_;
    }

    public Iterable<TelephoneNumber> modifyTelephoneNumbers(
            final Predicate<TelephoneNumber, IllegalStateException> predicate,
            final Modifier<TelephoneNumber, IllegalStateException> modifier) {
        final List<TelephoneNumber> result_ = new ArrayList<>();

        for (TelephoneNumber t_ : __telephoneNumbers) {
            if (predicate.match(t_)) {
                if (removeTelephoneNumber(t_)) {
                    final TelephoneNumber mt_ = modifier.modify(t_);
                    if (addTelephoneNumber(mt_)) {
                        result_.add(mt_);
                    }
                }
            }
        }

        return result_;
    }

    public final Iterable<Address> getAddresses() {
        // make defensive copies for thread safety
        final List<Address> result_ = new ArrayList<>();

        for (Address a_ : __addresses) {
            result_.add(new Address(a_));
        }

        return unmodifiableList(result_);
    }

    public final boolean addAddress(final Address address) {
        return __addresses.add(address);
    }

    public final Iterable<Address> addAddresses(final Iterable<Address> addresses) {
        final List<Address> result_ = new ArrayList<>();

        for (final Address a_ : addresses) {
            if (addAddress(a_)) {
                result_.add(a_);
            }
        }

        return result_;
    }

    public final Iterable<Address> addAddresses(final Address... addresses) {
        final List<Address> result_ = new ArrayList<>();

        for (final Address a_ : addresses) {
            if (addAddress(a_)) {
                result_.add(a_);
            }
        }

        return result_;
    }

    public final boolean removeAddress(final Address address) {
        return __addresses.remove(address);
    }

    public final Iterable<Address> removeAddresses(
            final Predicate<Address, IllegalStateException> predicate) {
        // how should a predicate behave? should it throw any exceptions?
        final List<Address> result_ = new ArrayList<>();

        for (Address a_ : __addresses) {
            if (predicate.match(a_)) {
                if (removeAddress(a_)) {
                    result_.add(a_);
                }
            }
        }

        return result_;
    }

    public Iterable<Address> findAddresses(
            final Predicate<Address, IllegalStateException> predicate) {
        final List<Address> result_ = new ArrayList<>();

        for (Address a_ : __addresses) {
            if (predicate.match(a_)) {
                result_.add(a_);
            }
        }

        return result_;
    }

    public Iterable<Address> modifyAddresses(
            final Predicate<Address, IllegalStateException> predicate,
            final Modifier<Address, IllegalStateException> modifier) {
        final List<Address> result_ = new ArrayList<>();

        for (Address a_ : __addresses) {
            if (predicate.match(a_)) {
                if (removeAddress(a_)) {
                    final Address ma_ = modifier.modify(a_);
                    if (addAddress(ma_)) {
                        result_.add(ma_);
                    }
                }
            }
        }

        return result_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringBuilder(this)
                .append("Id", getId())
                .append("Version", getVersion())
                .append("Email Addresses", __emailAddresses)
                .append("Telephone Numbers", __telephoneNumbers)
                .append("Addresses", __addresses)
                .append("super", super.toString())
                .build();
    }
}
