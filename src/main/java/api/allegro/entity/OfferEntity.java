package api.allegro.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "offer")
public class OfferEntity {
    @Id
    public String id;
    public String name;
    public String additionalServices;
    public String afterSalesServices;
    public String attachments;
    public String category;
    public String compatibilityList;
    public String contact;
    public String createdAt;
    public String customParameters;
    public String delivery;
    public String description;
    public String discounts;
    public String external;
    public String fundraisingCampaign;
    public String images;
    public String location;
    public String parameters;
    public String payments;
    public String product;
    public String promotion;
    public String publication;
    public String sellingMode;
    public String tax;
    public String sizeTable;
    public String stock;
    public String tecdocSpecification;
    public String b2b;
    public String updatedAt;
    public String validation;
    @Lob
    public HashMap<String, List<String>> paramMap;
    @Lob
    public ArrayList<String> categories;


    public OfferEntity() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompatibilityList() {
        return compatibilityList;
    }

    public void setCompatibilityList(String compatibilityList) {
        this.compatibilityList = compatibilityList;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getFundraisingCampaign() {
        return fundraisingCampaign;
    }

    public void setFundraisingCampaign(String fundraisingCampaign) {
        this.fundraisingCampaign = fundraisingCampaign;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getSellingMode() {
        return sellingMode;
    }

    public void setSellingMode(String sellingMode) {
        this.sellingMode = sellingMode;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getSizeTable() {
        return sizeTable;
    }

    public void setSizeTable(String sizeTable) {
        this.sizeTable = sizeTable;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTecdocSpecification() {
        return tecdocSpecification;
    }

    public void setTecdocSpecification(String tecdocSpecification) {
        this.tecdocSpecification = tecdocSpecification;
    }

    public String getB2b() {
        return b2b;
    }

    public void setB2b(String b2b) {
        this.b2b = b2b;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(String additionalServices) {
        this.additionalServices = additionalServices;
    }

    public String getAfterSalesServices() {
        return afterSalesServices;
    }

    public void setAfterSalesServices(String afterSalesServices) {
        this.afterSalesServices = afterSalesServices;
    }

    public Map<String, List<String>> getParamMap() {
        return paramMap;
    }

    public void setParamMap(HashMap<String, List<String>> paramMap) {
        this.paramMap = paramMap;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return name;
    }
}
