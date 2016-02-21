package com.coolmind.ordertracker.web.app.controllers;

import com.coolmind.ordertracker.core.exception.ErrorCodeInstants;
import com.coolmind.ordertracker.core.model.Customer;
import com.coolmind.ordertracker.core.services.CustomerService;
import com.coolmind.ordertracker.web.app.dtos.AddressDTO;
import com.coolmind.ordertracker.web.app.dtos.CustomerDTO;
import com.coolmind.ordertracker.web.utils.TestUtil;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by LiemTran on 1/28/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }


    @Test
    public void testFindAll() throws Exception {
        Customer cus1 = new Customer("customer 1", "hcm", "vietnam", "0987654321");
        cus1.setId(1l);
        cus1.setCode("CUS-001");
        Customer cus2 = new Customer("customer 2", "hcm", "vietnam", "0987654321");
        cus2.setId(2l);
        cus2.setCode("CUS-002");

        when(customerService.findAll()).thenReturn(Lists.newArrayList(cus1, cus2));

        mockMvc.perform(get(CustomerController.API_URL + "/all")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].code", is(cus1.getCode())))
                .andExpect(jsonPath("$[0].name", is(cus1.getName())))
                .andExpect(jsonPath("$[0].city", is(cus1.getCity())))
                .andExpect(jsonPath("$[0].country", is(cus1.getCountry())))
                .andExpect(jsonPath("$[0].phone", is(cus1.getPhone())))
                .andExpect(jsonPath("$[0].desc", is(cus1.getDesc())))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].code", is(cus2.getCode())))
                .andExpect(jsonPath("$[1].name", is(cus2.getName())))
                .andExpect(jsonPath("$[1].city", is(cus2.getCity())))
                .andExpect(jsonPath("$[1].country", is(cus2.getCountry())))
                .andExpect(jsonPath("$[1].phone", is(cus2.getPhone())))
                .andExpect(jsonPath("$[1].desc", is(cus2.getDesc())));

        verify(customerService, times(1)).findAll();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void findByCode() throws Exception {
        Customer cus1 = new Customer("customer 1", "hcm", "vietnam", "0987654321");
        cus1.setId(1l);
        cus1.setCode("CUS-001");

        when(customerService.findByCode(cus1.getCode())).thenReturn(cus1);

        mockMvc.perform(get(CustomerController.API_URL + "/CUS-001")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is(cus1.getCode())))
                .andExpect(jsonPath("$.name", is(cus1.getName())))
                .andExpect(jsonPath("$.city", is(cus1.getCity())))
                .andExpect(jsonPath("$.country", is(cus1.getCountry())))
                .andExpect(jsonPath("$.phone", is(cus1.getPhone())))
                .andExpect(jsonPath("$.desc", is(cus1.getDesc())));

        verify(customerService, times(1)).findByCode(cus1.getCode());
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void findByCodeNotFound() throws Exception {

        when(customerService.findByCode(anyString())).thenReturn(null);

        MvcResult result = mockMvc.perform(get(CustomerController.API_URL + "/CUS-001")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andReturn();

        assertEquals(ErrorCodeInstants.NOT_FOUND, result.getResponse().getContentAsString());
        verify(customerService, times(1)).findByCode("CUS-001");
        verifyNoMoreInteractions(customerService);
    }


    @Test
    public void testCreateCustomer() throws Exception {

        CustomerDTO cusDTO = new CustomerDTO("customer 1", "hcm", "vietnam", "0987654321","this is cus 1");
        cusDTO.setCode("CUS-001");

        AddressDTO addressDTO1 = new AddressDTO(1, "mr test 1", "quang trung 1", "hcm", "0987654321","email@.com","");
        AddressDTO addressDTO2 = new AddressDTO(2, "mr test 2", "quang trung 2", "hcm", "0987654321","email@.com","");

        cusDTO.setAddressList(Lists.newArrayList(addressDTO1, addressDTO2));

        Customer cus = CustomerDTO.toCustomer(cusDTO);

        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

        when(customerService.createCustomer(captor.capture())).thenReturn(cusDTO.getCode());

        MvcResult result =
                mockMvc.perform(post(CustomerController.API_URL)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(cusDTO))
                )
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(cusDTO.getCode(), content);
        Customer createdCus = captor.getValue();
        assertEquals(cus.toString(), createdCus.toString());
        assertEquals(2, createdCus.getAddressSet().size());
        verify(customerService, times(1)).createCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testCreateCustomerThrowsException() throws Exception {

        CustomerDTO dto = new CustomerDTO();


        when(customerService.createCustomer(any(Customer.class)))
                .thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(post(CustomerController.API_URL)
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(dto))
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(customerService, times(1)).createCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testUpdateUser() throws Exception {

        CustomerDTO cusDTO = new CustomerDTO("customer 1", "hcm", "vietnam", "0987654321","this is cus 1");
        cusDTO.setId(1l);
        cusDTO.setCode("CUS-001");

        AddressDTO addressDTO1 = new AddressDTO(1, "mr test 1", "quang trung 1", "hcm", "0987654321","email@.com","");
        AddressDTO addressDTO2 = new AddressDTO(2, "mr test 2", "quang trung 2", "hcm", "0987654321","email@.com","");

        cusDTO.setAddressList(Lists.newArrayList(addressDTO1, addressDTO2));

        Customer cus1 = CustomerDTO.toCustomer(cusDTO);

        when(customerService.updateCustomer(any(Customer.class))).thenReturn(cus1);

        mockMvc.perform(post(CustomerController.API_URL + "/CUS-001")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cusDTO))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is(cus1.getCode())))
                .andExpect(jsonPath("$.name", is(cus1.getName())))
                .andExpect(jsonPath("$.city", is(cus1.getCity())))
                .andExpect(jsonPath("$.country", is(cus1.getCountry())))
                .andExpect(jsonPath("$.phone", is(cus1.getPhone())))
                .andExpect(jsonPath("$.desc", is(cus1.getDesc())));


        verify(customerService, times(1)).updateCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testUpdateUserThrowsException() throws Exception {
        CustomerDTO cusDTO = new CustomerDTO("customer 1", "hcm", "vietnam", "0987654321","this is cus 1");
        cusDTO.setCode("CUS-001");
        Customer cus = CustomerDTO.toCustomer(cusDTO);

        when(customerService.updateCustomer(any(Customer.class))).thenThrow(new Exception());

        MvcResult result =
                mockMvc.perform(post(CustomerController.API_URL + "/CUS-001")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(TestUtil.convertObjectToJsonBytes(cusDTO))
                )
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                        .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals(ErrorCodeInstants.UNIDENTIFIED, content);
        verify(customerService, times(1)).updateCustomer(any(Customer.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    public void testDeleteUser() throws Exception {

        CustomerDTO cusDTO = new CustomerDTO("customer 1", "hcm", "vietnam", "0987654321","this is cus 1");
        cusDTO.setId(1l);
        cusDTO.setCode("CUS-001");
        Customer cus = CustomerDTO.toCustomer(cusDTO);

        when(customerService.deleteCustomer(cusDTO.getCode())).thenReturn(cus);

        mockMvc.perform(delete(CustomerController.API_URL + "/" + cusDTO.getCode())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.code", is(cus.getCode())))
                .andExpect(jsonPath("$.name", is(cus.getName())))
                .andExpect(jsonPath("$.city", is(cus.getCity())))
                .andExpect(jsonPath("$.country", is(cus.getCountry())))
                .andExpect(jsonPath("$.phone", is(cus.getPhone())))
                .andExpect(jsonPath("$.desc", is(cus.getDesc())));


        verify(customerService, times(1)).deleteCustomer(cusDTO.getCode());
        verifyNoMoreInteractions(customerService);
    }



}